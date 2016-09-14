package br.com.trabalho.tg.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import br.com.trabalho.tg.core.model.SDLArea;

public interface AreaDB {
	
	JSONArray findByLocation(Long local, String point);
	
	JSONArray findIntersects(String point);
	
}
