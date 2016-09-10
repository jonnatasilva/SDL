package br.com.trabalho.tg.core.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.trabalho.tg.core.model.SDLArea;

public interface AreaDB {
	
	ArrayList<String> findByLocation(Long local, String point);
	
	ArrayList<String> findIntersects(List<SDLArea> areas, String point);
	
}
