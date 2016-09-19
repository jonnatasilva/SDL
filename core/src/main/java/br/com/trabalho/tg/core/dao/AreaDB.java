package br.com.trabalho.tg.core.dao;

import org.json.JSONArray;

public interface AreaDB {

	JSONArray findIntersectionByLocal(Long local, String point) throws Exception;
}
