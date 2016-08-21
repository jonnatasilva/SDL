package br.com.trabalho.tg.handling;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionHandling extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6417119347832196982L;
	
	private Logger log = Logger.getLogger(ExceptionHandling.class);
	
	@ResponseStatus(value=HttpStatus.CONFLICT, reason="Opps, houve algum problema!")
	@ExceptionHandler(Exception.class)
	public void aboutException(Exception e) {
		log.error(e);
	}
}
