package br.com.trabalho.tg.core.model;

import lombok.Data;

@Data
public class Usuario {
	
	private Long id;
	private Boolean isAdm;
	public Usuario(long id, boolean isAdm) {
		this.id = id;
		this.isAdm = isAdm;
	}

}
