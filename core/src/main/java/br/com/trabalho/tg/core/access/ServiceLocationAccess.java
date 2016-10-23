package br.com.trabalho.tg.core.access;

import org.springframework.stereotype.Service;

@Service
public interface ServiceLocationAccess {
	
	/* Retorna padrão de polygono para ser transformado em geometria */
	public Object getLocationByCodigoAndLocal(String codigo, long local);
	
	
}
