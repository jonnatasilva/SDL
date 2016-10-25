package br.com.trabalho.tg.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.dao.AreaIntersectionDAO;
import br.com.trabalho.tg.core.model.AreaLocal;
import br.com.trabalho.tg.core.model.SDLArea;

@Service("sdlAreaService")
public class  AreaService {
	
	@Autowired
	private AreaDAO dao;
	
	@Autowired 
	private AreaIntersectionDAO daoIntersection;
	
	public SDLArea getAreaByCodigoAndIdLocal(String codigo, long local) throws Exception {
		return dao.findByCodigoAndIdLocal(codigo, local);
	}
	
	public SDLArea saveArea(SDLArea entity) throws Exception {
		return dao.save(entity);
	}
	
	public List<Object[]> getAreasByLocal(long idLocal) {
		return dao.findWithOutLocationByIdLocal(idLocal);
	}
	
	public void getIntesection(long local, String latitude, String longitude) {
		daoIntersection.findIntersectionByLocal(local, latitude, longitude);
	}
	
	public SDLArea getLocationByCodigoAndIdLocal(String codigo, long local) {
		SDLArea areaAux = new SDLArea();
		byte[] obj = dao.findLocaleByCodigoAndIdLocal(codigo, local);
		areaAux.setLocale(obj);
		return areaAux;
	}
	
	public List<AreaLocal> getAreasLocalByLocal(long idLocal) {
		List<Object[]> listAreas = dao.findCodigoAndDescricaoByLocal(idLocal);
		List<AreaLocal> listAux = new ArrayList<AreaLocal>();
		for(Object[] al : listAreas) {
			listAux.add(new AreaLocal((String) al[0], (String) al[1]));
		}
		return listAux;
	}
}
