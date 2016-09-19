package br.com.trabalho.tg.web.controller;

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

import br.com.trabalho.tg.core.handling.ExceptionHandling;
import br.com.trabalho.tg.core.impl.AreaDBIpml;
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import de.micromata.opengis.kml.v_2_2_0.Kml;

@Controller
@RequestMapping("/map/")
public class AreaController extends ExceptionHandling {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6778622757741017509L;

	@Autowired
	AreaService service;
	
	@Autowired
	HistoricoAreaService historicoService;
	
	@Autowired
	AreaDBIpml impl;

	private String prefixoPadrao = "redirect:/static";

	/*
	 * Met�do request irá retornar a p�gina default para a manipulação das
	 */
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	private ModelAndView iniciar() throws Exception {

//		impl.findByLocation(null, null);
		List<AreaLocal> areas = new ArrayList<AreaLocal>();
		for(Integer i = 1; i <= 3; i++) {
			AreaLocal areaLocal = new AreaLocal(i.toString(), "Area " + i);
			areas.add(areaLocal);
		}
		Local local = new Local((long) 1, "1", "Local Teste", "America/São", areas);
		ModelAndView model = new ModelAndView("/index");
		model.addObject("local", local);
		model.addObject("usuario", new UsuarioMock(1, "1","Jonnatas"));
		return model;
	}

	/* Salvar area no banco de dados */
	@RequestMapping(value = { "/save", "/salvar", "/criar" }, method = {
			RequestMethod.POST, RequestMethod.PUT })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> saveArea(@RequestParam("area") String obj)
			throws Exception {

		JSONObject json = new JSONObject(obj);

		SDLArea area = new SDLArea();
		area.setCodigo(json.getString("codigo"));
		area.setDescricao(json.getString("descricao"));
		String arrayString = json.getString("locale");
		area.setIdLocal(json.getLong("local"));
		area.setLocale(arrayString.getBytes());
		area.setLocation(GeometryUtils.arrayToPolygon(area.getLocaleArray()));

		area = service.saveArea(area);
		historicoService.save(new HistoricoArea(area.getLocale(), json.getLong("usuario"), area.getId()));
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	private Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader();
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (ParseException e) {
            throw new RuntimeException("Not a WKT string:" + wktPoint);
        }
        return geom;
    }

	@RequestMapping(value = "/list/json", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	private @ResponseBody List<SDLArea> listAreas(@RequestParam("idLocal") long idLocal) throws Exception {
		List<SDLArea> areas = new ArrayList<SDLArea>();
		List<Object[]> areasAux = new ArrayList<Object[]>();
		areasAux = service.getAreasByLocal(idLocal);
		if(areasAux != null) {
			for(Object[] a : areasAux) {
				SDLArea areaAux = new SDLArea();
				areaAux.setId((Long) a[0]);
				areaAux.setCodigo((String) a[1]);
				areaAux.setDescricao((String) a[2]);
				areaAux.setBackgroundColor((String) a[3]);
				areaAux.setBorderColor((String) a[4]);
				areaAux.setIdLocal((Long) a[5]);
				areaAux.setLocale((byte[]) a[6]);
				areas.add(areaAux);
			}
		}
		return areas;
	}

	@RequestMapping(value = "/parse/kml", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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
	
	@RequestMapping(value = "/teste/SQLServer/intersect", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody JSONArray testeIntersect(@RequestParam("lat") String latitude, @RequestParam("long") String longitude) {
		try {
			System.out.println(latitude);
			return impl.findIntersectionByLocal((long) 1, latitude + " " + longitude);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String getUrlView(MapeamentoEnum cView) {
		return prefixoPadrao.concat(cView.getString());
	}
}
