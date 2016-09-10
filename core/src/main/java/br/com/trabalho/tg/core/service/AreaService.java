package br.com.trabalho.tg.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.trabalho.tg.core.dao.AreaDAO;
import br.com.trabalho.tg.core.model.SDLArea;

@Service
public class AreaService {
	
	@Autowired
	private AreaDAO dao;
	
	public SDLArea getAreaByCodigo(String codigo) throws Exception {
		return dao.findByCodigo(codigo);
	}
	
	public SDLArea saveArea(SDLArea entity) throws Exception {
		return dao.save(entity);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<SDLArea> getAreasByLocal(long idLocal) throws Exception {
		return dao.findAll();
	}

}
