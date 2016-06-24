package br.com.trabalho.tg.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="area")
public class Area {
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="descricao", nullable=false)
	private String descricao;
	
	@Column(name="locale", nullable=false)
	private String locale;
	
	@Transient
	private Object[] localeObj;

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

	public Object[] getLocaleObj() {
		String obj[] = locale.split("],");
		Double obj2[] = new Double[2];
		
		localeObj = new Object[obj.length];
		for(int i = 0; i < obj.length; i++) {
			String objAux = obj[i].replace("]", "");
			objAux = objAux.replace("[", "");
			
			String array[] = objAux.split(",");
			obj2[0] = Double.parseDouble(array[0]);
			obj2[1] = Double.parseDouble(array[1]);
			localeObj[i] = obj2; 
			
		}
		return localeObj;
	}
	
	

	
}
