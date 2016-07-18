package br.com.trabalho.tg.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.com.trabalho.tg.enums.MapeamentoEnum;
import br.com.trabalho.tg.handling.ExceptionHandling;
import br.com.trabalho.tg.service.AreaService;
import br.com.trabalho.tg.vo.Area;

@Controller
@RequestMapping("/map/polygon")
public class AreaController extends ExceptionHandling {

	@Autowired
	AreaService service;

	private String prefixoPadrao = "redirect:/static";
	private ModelAndView model;

	/*
	 * Metódo request irá retornar a página default para a manipulação das
	 * areas,é necessário passar código e descrição da area
	 */
	@RequestMapping(value = { "", "/" }, params = { "codigo", "descricao" }, method = RequestMethod.GET)
	private ModelAndView iniciar(@RequestParam("codigo") String codigo,
			@RequestParam("descricao") String descricao) throws Exception {
		model = new ModelAndView(getUrlView(MapeamentoEnum.INICIAR));
		Area area = service.getAreaByCodigo(codigo);
		model.addObject("localeArea", area.getLocale());
		model.addObject("codigoArea", area.getCodigo());
		model.addObject("descricaoArea", area.getDescricao());

		return model;
	}

	/* Salvar area no banco de dados */
	@RequestMapping(value = { "/save", "/salvar", "/criar" }, method = {
			RequestMethod.POST, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> saveArea(@RequestBody Object obj)
			throws Exception {

		LinkedHashMap<String, String> dados = (LinkedHashMap<String, String>) obj;
		Area area = new Area();
		area.setCodigo(dados.get("codigo"));
		area.setDescricao(String.valueOf(dados.get("descricao")));
		area.setLocale(String.valueOf(dados.get("locale")));

		service.saveArea((Area) area);

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/list", params={"idLocal"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	private ResponseEntity<Object> listAreas(@RequestParam("idLocal") Long idLocal) throws Exception {
		List<Area> areas = new ArrayList<Area>();
		areas = service.getAreasByLocal(idLocal);
		areas.get(0).getLocaleObj();
		return new ResponseEntity<Object>(areas, HttpStatus.OK);
	}

	private String getUrlView(MapeamentoEnum cView) {
		return prefixoPadrao.concat(cView.getString());
	}
	
	public static void main (String args[])  {
		InetAddress ipAddress;
		try {
			ipAddress = InetAddress.getLocalHost();
			System.out.println(ipAddress.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
