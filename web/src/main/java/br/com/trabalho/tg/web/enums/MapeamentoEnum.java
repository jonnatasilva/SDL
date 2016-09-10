package br.com.trabalho.tg.web.enums;

public enum MapeamentoEnum {
	
	INICIAR("/map.html");
	
	private String valor;
	
	private MapeamentoEnum(String valor) {
		this.valor = valor;
	}
	
	public String getString() {
		return this.valor.toString();
	}

}
