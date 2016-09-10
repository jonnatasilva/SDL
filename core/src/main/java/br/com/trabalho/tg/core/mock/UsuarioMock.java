package br.com.trabalho.tg.core.mock;

import lombok.Data;

@Data
public class UsuarioMock {
	
	private Long id;
	private String codigo;
	private String nome;
	
	public UsuarioMock(long id, String codigo, String nome) {
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
	}
}
