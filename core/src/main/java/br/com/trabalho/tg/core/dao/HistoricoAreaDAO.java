package br.com.trabalho.tg.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.model.HistoricoArea;

@Repository
public interface HistoricoAreaDAO extends JpaRepository<HistoricoArea, Long> {
	
}