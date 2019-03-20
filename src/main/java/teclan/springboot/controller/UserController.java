package teclan.springboot.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teclan.springboot.dao.Users1Mapper;
import teclan.springboot.entity.Users1;

@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private Users1Mapper users1Mapper;

	@RequestMapping("/get")
	public Users1 get(Long id) {
		return users1Mapper.selectByPrimaryKey(id);
	}

	@RequestMapping("/findByCode")
	public Map<String, Object> findByCode(String code) {
		return jdbcTemplate.queryForMap("select * from user_info where code=?",code);
	}


}
