package br.com.trabalho.tg.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trabalho.tg.core.model.HistoricoArea;

public interface HistoricoAreaDAO extends JpaRepository<HistoricoArea, Long> {
	
}
