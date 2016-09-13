package br.com.trabalho.tg.core.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.dao.AreaDB;
import br.com.trabalho.tg.core.model.SDLArea;

@Repository
public class SQLServerIpml implements AreaDB {

	@Autowired
	private AreaDAO areaDAO;
	
	protected EntityManager manager;

	//Metodo utilizado para retornar areas com interseção com um ponto
	public ArrayList<String> findByLocation(Long local, String point) {
		//Retornar todas as areas para o local
		List<SDLArea> areas = areaDAO.findByIdLocal((long) 1);
		return this.findIntersects(areas, point);
	}
	public ArrayList<String> findIntersects(List<SDLArea> areas, String point) {
		ArrayList<String> result = new ArrayList<String>();
		for(SDLArea a : areas) {
			StringBuffer sb = new StringBuffer("select geometry::STPolyFromText('POLYGON ((");
			sb.append("-4862617.991389773 -931308.7526265874, -5180596.029056106 -1201590.0846429705, -4551977.908438817 -1128210.5374892014, -4857726.021579522 -945984.6620573412, -4862617.991389773 -931308.7526265874");
			sb.append("))', 0).STIntersects(geometry::STPointFromText('POINT (");
			sb.append(point);
			sb.append(")', 0)) as geom");
			Query query = (Query) this.manager.createQuery(sb.toString(), Integer.class);
			
			if(String.valueOf(query.getSingleResult()).equals("1")) {
				result.add(a.getCodigo());
			}
		}
		return result;
	}
	
	@PersistenceContext
	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

}
