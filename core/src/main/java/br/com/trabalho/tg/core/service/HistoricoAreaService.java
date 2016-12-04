package br.com.trabalho.tg.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trabalho.tg.core.dao.HistoricoAreaDAO;
import br.com.trabalho.tg.core.model.HistoricoArea;

@Service("sdlHistoricoService")
public class HistoricoAreaService {

	@Autowired
	private HistoricoAreaDAO dao;
	
	public List<HistoricoArea> findAll() {
		return dao.findAll();
	}
	
	public void save(HistoricoArea historicoArea) {
		dao.save(historicoArea);
	}
	
	public void deleteFromArea(long idArea) {
		dao.deleteFromArea(idArea);
	}

}
