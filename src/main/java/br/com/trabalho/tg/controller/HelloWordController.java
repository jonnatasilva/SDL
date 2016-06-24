package br.com.trabalho.tg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloWordController {
	
	@RequestMapping("/")
	public String index() {
		return "redirect:/static/front/index.html";
	}

}
