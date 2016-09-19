package br.com.trabalho.tg.core.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.dao.AreaDB;
import br.com.trabalho.tg.core.model.SDLArea;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

@Repository
public class AreaDBIpml implements AreaDB{

	@Autowired
	private AreaDAO areaDAO;

	protected EntityManager manager;

	// Metodo utilizado para retornar areas com interseção com um ponto
	public JSONArray findIntersectionByLocal(Long local, String point) throws Exception {
		// Retornar todas as areas para o local
		List<SDLArea> areas = areaDAO.findByIdLocal(local);
		Collections.sort(areas);
		return this.findIntersects(point, areas);
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

	@PersistenceContext
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}
}