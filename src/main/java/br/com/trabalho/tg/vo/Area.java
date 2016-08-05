package br.com.trabalho.tg.vo;

import java.io.UnsupportedEncodingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.json.JSONArray;


@Entity
@Table(name="area", uniqueConstraints={@UniqueConstraint(columnNames={"codigo"})})
public class Area {
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="descricao", nullable=false)
	private String descricao;
	
	@Column(name="locale", nullable=false)
	private byte[] locale;
	
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

	public byte[] getLocale() {
		return locale;
	}

	public void setLocale(byte[] locale) {
		this.locale = locale;
	}

	public JSONArray getLocaleArray() throws UnsupportedEncodingException {
		String str = new String(this.locale, "UTF-8");
		return new JSONArray("[" + str + "]");
	}
}	
