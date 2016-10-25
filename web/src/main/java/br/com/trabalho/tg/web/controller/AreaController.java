package br.com.trabalho.tg.web.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.trabalho.tg.core.impl.AreaDBImpl;
import br.com.trabalho.tg.core.mock.UsuarioMock;
import br.com.trabalho.tg.core.model.AreaLocal;
import br.com.trabalho.tg.core.model.HistoricoArea;
import br.com.trabalho.tg.core.model.Local;
import br.com.trabalho.tg.core.model.SDLArea;
import br.com.trabalho.tg.core.service.AreaService;
import br.com.trabalho.tg.core.service.HistoricoAreaService;
import br.com.trabalho.tg.core.utils.GeometryUtils;
import br.com.trabalho.tg.core.utils.KmlUtils;
import br.com.trabalho.tg.web.enums.MapeamentoEnum;
import de.micromata.opengis.kml.v_2_2_0.Kml;

@Controller("sdlAreaController")
@RequestMapping("/map/")
@Log4j
public class AreaController {

	@Autowired
	AreaService service;
	
	@Autowired
	HistoricoAreaService historicoService;
	
	@Autowired
	AreaDBImpl impl;

	private String prefixoPadrao = "redirect:/static";
	
	private List<AreaLocal> areas = new ArrayList<AreaLocal>();

	/*
	 * Metódo irá retornar a página default para a manipulação das
	 * @param local, id do local de trabalho
	 * @param areas, areas do local de trabalho {codigo, descricao}
	 * @param usuario, id do usuario que está usando o sistema requisitante
	 * @param latInicial, latitude onde o mapa deve abrir ao iniciarm caso não tenha nenhuma área geográfica csdastrada para o local
	 * @param longInicial longitude ''  			''						''
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	private ModelAndView iniciar(@RequestParam("local") long idLocal, @RequestParam("areas") JSONArray ars, 
			@RequestParam("usuario") long idUsuario, @RequestParam("latInicial") long lat, @RequestParam("longInicial") long inicial) {
		ModelAndView model = new ModelAndView("/index");
		try {
			List<AreaLocal> areas = new ArrayList<AreaLocal>();
			for(int i = 0; i < ars.length(); i++) {
				AreaLocal areaLocal = new AreaLocal(ars.getJSONObject(i).get("codigo").toString(), ars.getJSONObject(i).getString("descricao"));
				areas.add(areaLocal);
			}
			Local local = new Local(idLocal, "1", "Local Teste", "America/São", areas, 
					service.getAreasLocalByLocal(idLocal));
			model.addObject("local", local);
			model.addObject("usuario", new UsuarioMock(idUsuario, "1","Jonnatas"));
		}catch (Exception e) {
			log.error(e);
		}
		return model;
	}

	/* Salvar area no banco de dados */
	@RequestMapping(value = { "/save", "/salvar", "/criar" }, method = {RequestMethod.POST, RequestMethod.PUT})
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> saveArea(@RequestParam("area") String obj) {
		try {
			JSONObject json = new JSONObject(obj);
			
			SDLArea area = null;
			if(json.has("codigo") && json.has("local")) {
				area = service.getAreaByCodigoAndIdLocal(json.getString("codigo"), json.getLong("local"));
				if(area == null) {
					area = new SDLArea();
					area.setCodigo(json.getString("codigo"));
					area.setIdLocal(json.getLong("local"));
				} 
				area.setDescricao(json.getString("descricao"));
				String arrayString = json.getString("locale");
				area.setLocale(arrayString.getBytes());
				System.out.println(arrayString.getBytes().length);
				area.setLocation(GeometryUtils.arrayToPolygon(area.getLocaleArray()));
				area = service.saveArea(area);
				historicoService.save(new HistoricoArea(area.getLocale(), json.getLong("usuario"), area.getId()));
			}			
		}catch (Exception e) {
			log.error("Falha ao salvar Area: " + e);
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/list/json", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody List<SDLArea> listAreas(@RequestParam("idLocal") long idLocal) {
		List<SDLArea> areas = new ArrayList<SDLArea>();
		try{
			List<Object[]> areasAux = new ArrayList<Object[]>();
			areasAux = service.getAreasByLocal(idLocal);
			if(areasAux != null) {
				for(Object[] a : areasAux) {
					SDLArea areaAux = new SDLArea();
					areaAux.setId((Long) a[0]);
					areaAux.setCodigo((String) a[1]);
					areaAux.setDescricao((String) a[2]);
					areaAux.setIdLocal((Long) a[3]);
					areaAux.setLocale((byte[]) a[4]);
					areas.add(areaAux);
					log.info("Area listada: " + areaAux.getDescricao());
				} 
			}
		} catch (Exception e) {
			log.error("Falha ao listar Areas: " + e);
		}
		return areas;
	}
	
	@RequestMapping(value = "/get/polygon/json", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody SDLArea listAreas(@RequestParam("idLocal") long idLocal, 
			@RequestParam("codigo") String codigoArea) throws Exception {
		return service.getLocationByCodigoAndIdLocal(codigoArea, idLocal);
	}
	
	@RequestMapping(value = "/parse/kml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody String parseKML(@RequestParam("kml") MultipartFile file) {
		JSONObject obj = null;
		try {
			if (!file.isEmpty()) {
				Kml kml = Kml.unmarshal(file.getInputStream());
				JSONArray array = KmlUtils.parse(kml.getFeature());
				obj = (JSONObject) array.get(0);
			}
		}catch (Exception e) {
			log.error("Falha ao parsear KML: " + e);
		}
		return obj.toString();
	}
	
	@RequestMapping(value = "/intersection", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONArray intersection(@RequestParam("lat") String latitude, @RequestParam("long") String longitude,
			@RequestParam("local") long local) {
		try {
			return impl.findIntersectionByLocal(local, latitude, longitude);
		} catch (Exception e) {
			log.error("Falha ao buscar verificar intersecção: " + e);
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String getUrlView(MapeamentoEnum cView) {
		return prefixoPadrao.concat(cView.getString());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> excpetionHandler() {
		return new ResponseEntity<String>(HttpStatus.CONFLICT);
	}
}