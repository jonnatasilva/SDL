package br.com.trabalho.tg.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.model.SDLArea;

@Repository
public interface AreaDAO extends JpaRepository<SDLArea, Long>{
	
	SDLArea findByDescricao(String descricao);
	
	SDLArea findByCodigo(String codigo);
	
	List<SDLArea> findByIdLocal(Long local);
	

}
