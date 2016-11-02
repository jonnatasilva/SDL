package br.com.trabalho.tg.core.impl;

import org.springframework.stereotype.Component;

import br.com.trabalho.tg.core.access.ServiceLocationAccess;
import br.com.trabalho.tg.core.service.AreaService;

@Component
public class ServiceLocationAccessImpl implements ServiceLocationAccess {
	
	private AreaService service;
	
	public Object getLocationByCodigoAndLocal(String codigo, long local) {
		return service.getLocationByCodigoAndIdLocal(codigo, local);
	}
	
}
