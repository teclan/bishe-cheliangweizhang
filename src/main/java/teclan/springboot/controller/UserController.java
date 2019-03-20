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
public class UserController {

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private Users1Mapper users1Mapper;
	
	@RequestMapping("/user/query")
	public List<Map<String,Object>> query() {
		return jdbcTemplate.queryForList("select * from users limit 1,3");
	}

	@RequestMapping("/user/get")
	public Users1 get(Long id) {
		return users1Mapper.selectByPrimaryKey(id);
	}


	@RequestMapping("/user/findOne")
	public Users1 findOne(Long id) {
		return users1Mapper.findOne(id);
	}


}
