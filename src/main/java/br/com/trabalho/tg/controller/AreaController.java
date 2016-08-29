package br.com.trabalho.tg.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.trabalho.tg.enums.MapeamentoEnum;
import br.com.trabalho.tg.handling.ExceptionHandling;
import br.com.trabalho.tg.service.AreaService;
import br.com.trabalho.tg.utils.KmlUtils;
import br.com.trabalho.tg.vo.Area;
import de.micromata.opengis.kml.v_2_2_0.Kml;

@Controller
@RequestMapping("/map/polygon")
public class AreaController extends ExceptionHandling {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6778622757741017509L;

	@Autowired
	AreaService service;

	private String prefixoPadrao = "redirect:/static";

	/*
	 * Metódo request irá retornar a página default para a manipulação das
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	private ModelAndView iniciar() throws Exception {
		return new ModelAndView(getUrlView(MapeamentoEnum.INICIAR));
	}

	/* Salvar area no banco de dados */
	@RequestMapping(value = { "/save", "/salvar", "/criar" }, method = {
			RequestMethod.POST, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> saveArea(@RequestParam("area") String obj)
			throws Exception {

		JSONObject json = new JSONObject(obj);

		Area area = new Area();
		area.setCodigo(json.getString("codigo"));
		area.setDescricao(json.getString("descricao"));
		String arrayString = json.getString("locale");
		area.setLocale(arrayString.getBytes());

		service.saveArea((Area) area);

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/list", params = { "idLocal" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	private ResponseEntity<Object> listAreas(
			@RequestParam("idLocal") Long idLocal) throws Exception {
		List<Area> areas = new ArrayList<Area>();
		areas = service.getAreasByLocal(idLocal);
		areas.get(0).getLocaleArray();
		return new ResponseEntity<Object>(areas, HttpStatus.OK);
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody List<Area> listAreas() throws Exception {
		List<Area> areas = new ArrayList<Area>();
		areas = service.getAreasByLocal(0);

		return areas;
	}

	@RequestMapping(value = "/parseKML", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody String parseKML(@RequestParam("kml") MultipartFile file) throws Exception {
		JSONObject obj = null;
		if (!file.isEmpty()) {
			Kml kml = Kml.unmarshal(file.getInputStream());
			JSONArray array = KmlUtils.parse(kml.getFeature());
			obj = (JSONObject) array.get(0);
		}
		return obj.toString();
	}

	private String getUrlView(MapeamentoEnum cView) {
		return prefixoPadrao.concat(cView.getString());
	}

	public static void main(String args[]) {
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
