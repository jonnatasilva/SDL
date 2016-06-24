package br.com.trabalho.tg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.vo.Area;

@Repository
public interface AreaDAO extends JpaRepository<Area, Long>{
	
	Area findByDescricao(String descricao);
	
	Area findByCodigo(String codigo);
	

}
