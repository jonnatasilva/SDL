package br.com.trabalho.tg.core.model;

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
@Table(name="sdl_area", uniqueConstraints={
		@UniqueConstraint(columnNames={"codigo", "id_local"})
})

public class SDLArea {
	
	@Id
	@GeneratedValue
	protected Long id;
	
	@Column(name="codigo")
	protected String codigo;
	
	@Column(name="descricao", nullable=false)
	protected String descricao;
	
	@Column(name="locale", nullable=false)
	protected byte[] locale;
	
	@Column(name="background_color", nullable=true)
	private String backgroundColor;
	
	@Column(name="border_color", nullable=true)
	protected String borderColor;
	
	@Column(name="id_local", nullable=true)
	protected Long idLocal;
	
	@Transient
	private Object[] localeObj;

	public JSONArray getLocaleArray() throws UnsupportedEncodingException {
		String str = new String(this.locale, "UTF-8");
		return new JSONArray("[" + str + "]");
	}
}	
