package br.com.trabalho.tg.vo;

import java.io.UnsupportedEncodingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.Data;

import org.json.JSONArray;

@Entity
@Data
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
	
	@Column(name="backgroundColor", nullable=true)
	private String backgroundColor;
	
	@Column(name="borderColor", nullable=true)
	private String borderColor;
	
	@Transient
	private Object[] localeObj;

	public JSONArray getLocaleArray() throws UnsupportedEncodingException {
		String str = new String(this.locale, "UTF-8");
		return new JSONArray("[" + str + "]");
	}
	
}	
