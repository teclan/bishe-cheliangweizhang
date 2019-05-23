package teclan.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teclan.springboot.log.LogModule;
import teclan.springboot.log.LogService;
import teclan.springboot.log.LogStatus;
import teclan.springboot.utils.IdUtils;
import teclan.springboot.utils.ResultUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {
    private Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Value("${static.file.dir}")
    private String staticFileDir;
    @Resource
    private LogService logService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/image/upload")
    public JSONObject fileUpload(@RequestParam(value = "file") MultipartFile file,String id, HttpServletRequest httpServletRequest) {

        String user = httpServletRequest.getHeader("user");

        if (file.isEmpty()) {
            LOGGER.error("{} 上传文件失败,文件为空", user);
            logService.add(LogModule.violationManage, user, String.format("添加违章图片信息"), LogStatus.fail);
            return ResultUtils.get(500, "上传失败", "");


        }
        String fileName = file.getOriginalFilename();  // 文件名
        fileName = IdUtils.get() + "_" + fileName; // 新文件名
        File dest = new File(staticFileDir + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            logService.add(LogModule.violationManage, user, String.format("添加违章图片信息,%s",e.getMessage()), LogStatus.fail);
            return ResultUtils.get(500,"上传失败", null);
        }

        String url="/resource/"+fileName;
        Map<String, Object> datas = new HashMap<>();
        datas.put("fileName", fileName);
        datas.put("url", url);

        if(!StringUtils.isNullOrEmpty(id)){
            jdbcTemplate.update("update violation set url=? where id=?",url,id);
        }
        logService.add(LogModule.violationManage, user, String.format("添加违章图片信息:%s", datas.toString()), LogStatus.success);

        return ResultUtils.get("上传成功", datas);
    }
}
