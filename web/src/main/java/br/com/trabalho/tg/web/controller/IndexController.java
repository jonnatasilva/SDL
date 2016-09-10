package br.com.trabalho.tg.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.trabalho.tg.core.mock.UsuarioMock;
import br.com.trabalho.tg.core.model.AreaLocal;
import br.com.trabalho.tg.core.model.Local;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping("/")
	public ModelAndView iniciar() throws Exception {
		
		List<AreaLocal> areas = new ArrayList<AreaLocal>();
		for(Integer i = 1; i <= 3; i++) {
			AreaLocal areaLocal = new AreaLocal(i.toString(), "Area " + i);
			areas.add(areaLocal);
		}
		Local local = new Local((long) 1, "1", "Local Teste", "America/São", areas);
		ModelAndView model = new ModelAndView("redirect:/static/index.html");
		model.addObject("local", local);
		model.addObject("usuario", new UsuarioMock(1, "Jonnatas"));
		return model;
		
	}
}
