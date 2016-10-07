package br.com.trabalho.tg.core.dao;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component("areaIntersectionDAO")
public interface AreaIntersectionDAO {

	/*
	 * Metódo utilizado para retornar areas que manntém uma interseção com um determinado ponto
	 * @param local id do local a quem as areas estão vinculadas
	 * @param latitude referente ao ponto
	 * @param longitude referente ao ponto
	 * */
	JSONArray findIntersectionByLocal(Long local, String latitude, String longitude);
}
