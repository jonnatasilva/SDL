package br.com.trabalho.tg.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.trabalho.tg.core.model.SDLArea;

@Repository
public interface AreaDAO extends JpaRepository<SDLArea, Long>{
	
	SDLArea findByDescricao(String descricao);
	
	SDLArea findByCodigo(String codigo);
	
	@Query("select t.id, t.codigo, t.descricao, t.backgroundColor, t.borderColor, t.idLocal, t.locale from #{#entityName} t where t.idLocal = ?1")
	List<Object[]> findWithOutLocationByIdLocal(Long local);
	

}
