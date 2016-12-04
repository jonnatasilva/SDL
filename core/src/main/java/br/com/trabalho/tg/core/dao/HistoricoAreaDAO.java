package br.com.trabalho.tg.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.model.HistoricoArea;

@Repository("sdlHistoricoAreaDAO")
public interface HistoricoAreaDAO extends JpaRepository<HistoricoArea, Long> {
	
	@Query("delete from HistoricoArea t where t.area.id = ?1")
	void deleteFromArea(long idArea);
	
}
