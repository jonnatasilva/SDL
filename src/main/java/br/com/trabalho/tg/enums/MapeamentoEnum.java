package br.com.trabalho.tg.enums;

public enum MapeamentoEnum {
	
	INICIAR("/front/map.html");
	
	private String valor;
	
	private MapeamentoEnum(String valor) {
		this.valor = valor;
	}
	
	public String getString() {
		return this.valor.toString();
	}

}
