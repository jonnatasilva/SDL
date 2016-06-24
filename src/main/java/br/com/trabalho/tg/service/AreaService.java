package br.com.trabalho.tg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trabalho.tg.dao.AreaDAO;
import br.com.trabalho.tg.vo.Area;

@Service
public class AreaService {
	
	@Autowired
	private AreaDAO dao;
	
	public Area getAreaByCodigo(String codigo) throws Exception {
		return dao.findByCodigo(codigo);
	}
	
	public Area saveArea(Area entity) throws Exception {
		return dao.save(entity);
	}

	
	public List<Area> getAreasByLocal(long idLocal) throws Exception {
		return dao.findAll();
	}

}
