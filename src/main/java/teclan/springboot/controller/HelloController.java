package teclan.springboot.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import teclan.springboot.model.Hello;

@RestController  
public class HelloController {
	
	@RequestMapping("/hello")
	public Hello hello() {
		return new Hello("default");
	}
	
	@RequestMapping(value="/hello1/{name}",method = RequestMethod.POST)
	public Hello hello1(@PathVariable("name") String name) {
		return new Hello(name);
	}
	
	@RequestMapping("/hello2")
	public Hello hello2(@RequestParam(value="name", defaultValue="teclan") String name) {
		return new Hello(name);
	}

}
