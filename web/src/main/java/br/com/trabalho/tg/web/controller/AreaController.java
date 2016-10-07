package br.com.trabalho.tg.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import br.com.trabalho.tg.web.enums.MapeamentoEnum;
import br.com.trabalho.tg.web.utils.GeometryUtils;
import br.com.trabalho.tg.web.utils.KmlUtils;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import lombok.extern.log4j.Log4j;

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

	/*
	 * Met�do request irá retornar a p�gina default para a manipulação das
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	private ModelAndView iniciar() {
		File file = new File("global.properties");
		ModelAndView model = new ModelAndView("/index");
		try {
			List<AreaLocal> areas = new ArrayList<AreaLocal>();
			for(Integer i = 1; i <= 3; i++) {
				AreaLocal areaLocal = new AreaLocal(i.toString(), "Area " + i);
				areas.add(areaLocal);
			}
			Local local = new Local((long) 1, "1", "Local Teste", "America/São", areas);
			model.addObject("local", local);
			model.addObject("usuario", new UsuarioMock(1, "1","Jonnatas"));
		}catch (Exception e) {
			log.error(e);
		}
		return model;
	}

	/* Salvar area no banco de dados */
	@RequestMapping(value = { "/save", "/salvar", "/criar" }, method = {
			RequestMethod.POST, RequestMethod.PUT })
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
	
//	private Geometry wktToGeometry(String wktPoint) {
//        WKTReader fromText = new WKTReader();
//        Geometry geom = null;
//        try {
//            geom = fromText.read(wktPoint);
//        } catch (ParseException e) {
//            log.error("Not a WKT string:" + wktPoint);
//        }
//        return geom;
//    }

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
				}
			}
		} catch (Exception e) {
			log.error("Falha ao listar Areas: " + e);
		}
		return areas;
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