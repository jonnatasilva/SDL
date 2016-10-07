package br.com.trabalho.tg.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.dao.AreaIntersectionDAO;
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

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Object[]> getAreasByLocal(long idLocal) {
		return dao.findWithOutLocationByIdLocal(idLocal);
	}
	
	public void getIntesection(long local, String latitude, String longitude) {
		daoIntersection.findIntersectionByLocal(local, latitude, longitude);
	}
}
