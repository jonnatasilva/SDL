package br.com.trabalho.tg.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.trabalho.tg.core.model.AreaLocal;
import br.com.trabalho.tg.core.model.Local;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping("/")
	public String iniciar(RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		
		List<AreaLocal> areas = new ArrayList<AreaLocal>();
		for(Integer i = 1; i <= 3; i++) {
			AreaLocal areaLocal = new AreaLocal(i.toString(), "Area " + i);
			areas.add(areaLocal);
		}
		Local local = new Local((long) 1, "1", "Local Teste", "America/São", areas);
//		redirectAttributes.addAttribute("local", local.toString());
		redirectAttributes.addAttribute("local", local);
		request.setCharacterEncoding("UTF-8");
		return "redirect:/map/";
		
	}
}
