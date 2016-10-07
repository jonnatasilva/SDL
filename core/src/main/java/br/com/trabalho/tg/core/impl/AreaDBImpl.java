package br.com.trabalho.tg.core.impl;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.dao.AreaIntersectionDAO;
import br.com.trabalho.tg.core.model.SDLArea;
import lombok.extern.log4j.Log4j;

@Log4j
@Repository("sdlAreaDBImpl")
public class AreaDBImpl implements AreaIntersectionDAO {

	@Autowired
	private AreaDAO areaDAO;
	
	// Metodo utilizado para retornar areas com interseção com um ponto
	public JSONArray findIntersectionByLocal(Long local, String latiude, String longitude) {
		// Retornar todas as areas para o local
		try {
			List<SDLArea> areas = areaDAO.findByidLocal(local);
			Collections.sort(areas);
			return this.findIntersects(latiude + " " + longitude, areas);
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	public JSONArray findIntersects(String point, List<SDLArea> areas) throws Exception {
		Geometry geoPoint =  new WKTReader().read("POINT(" + point + ")");

		JSONArray array = new JSONArray();
		for(SDLArea a : areas) {
			if(!a.getLocation().intersection(geoPoint).isEmpty()) {
				array.put(a.getCodigo() + " - " + a.getLocation().getArea());
			}
		}
		return array;
	}
}