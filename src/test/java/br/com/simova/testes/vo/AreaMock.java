package br.com.simova.testes.vo;

import java.util.Random;

public class AreaMock {
	
	private Long id;
	
	private String codigo;
	
	private String descricao;
	
	private String locale;
	
	
	public AreaMock(String c, String d, String l) {
		this.id = new Random().nextLong();
		this.codigo = c;
		this.descricao = d;
		this.locale = l;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	

}
