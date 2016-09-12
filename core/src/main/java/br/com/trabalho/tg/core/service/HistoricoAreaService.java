package br.com.trabalho.tg.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.trabalho.tg.core.dao.HistoricoAreaDAO;
import br.com.trabalho.tg.core.handling.ExceptionHandling;
import br.com.trabalho.tg.core.model.HistoricoArea;

public class HistoricoAreaService extends ExceptionHandling{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8869281005285401639L;
	
	@Autowired
	private HistoricoAreaDAO dao;
	
	public List<HistoricoArea> findAll() {
		return dao.findAll();
	}
	
	public void save(HistoricoArea historicoArea) {
		dao.save(historicoArea);
	}

}
