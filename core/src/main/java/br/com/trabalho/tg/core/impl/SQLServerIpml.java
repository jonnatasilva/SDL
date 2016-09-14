package br.com.trabalho.tg.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.dao.AreaDB;
import br.com.trabalho.tg.core.model.SDLArea;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Repository
public class SQLServerIpml implements AreaDB{

	@Autowired
	private AreaDAO areaDAO;

	protected EntityManager manager;

	// Metodo utilizado para retornar areas com interseção com um ponto
	public JSONArray findByLocation(Long local, String point) {
		// Retornar todas as areas para o local
		// List<SDLArea> areas = null areaDAO.findByIdLocal((long) 1) ;;
		return this.findIntersects(point);
	}

	public JSONArray findIntersects(String point) {
		Geometry geo = null;
		Geometry geo2 = null;

		try {
			geo = new WKTReader().read("POINT(-6584591.364598222 -596820.3168506566)");
			geo2 = new WKTReader().read("POLYGON ((20 20, 50 30, 50 50, 30 50, 20 20))");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		StringBuffer sb = new StringBuffer("select a.loc from SDLArea a");
//		sb.append("(geometry::STPolygonFromTex(location).STIntersects(geometry::STPointFromText('POINT (");
//		sb.append(point);
//		sb.append(")', 0)) = TRUE");
		Query query = (Query) this.manager.createQuery("select a from SDLArea a order by a.id asc", SDLArea.class);
//		query.setParameter(0, );
//		Query query = (Query) manager.createNativeQuery(sb.toString());
		List<SDLArea> area = query.getResultList();
		Collections.sort(area);
		JSONArray array = new JSONArray();
		for(SDLArea a : area) {
			System.out.println(geo.intersection(a.getLocation()));
			System.out.println(a.getLocation().getArea());
			if(!geo.intersection(a.getLocation()).isEmpty()) {
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
