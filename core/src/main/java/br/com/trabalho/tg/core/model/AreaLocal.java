package br.com.trabalho.tg.core.model;

import lombok.Data;

@Data
public class AreaLocal {
	
	private String codigo;
	private String descricao;
	
	public AreaLocal(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
}
