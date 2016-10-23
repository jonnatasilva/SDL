package br.com.trabalho.tg.core.model;

import java.io.UnsupportedEncodingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

import org.hibernate.annotations.Type;
import org.json.JSONArray;

import br.com.trabalho.tg.core.utils.GeometryUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Geometry;

@Entity
@Data
@Table(name="sdl_area", uniqueConstraints={
		@UniqueConstraint(columnNames={"codigo", "id_local"})
})
@JsonIgnoreProperties(value = { "locale", "location" })
public class SDLArea implements Comparable<SDLArea>{
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="descricao", nullable=false)
	private String descricao;
	
	@Lob
	@Column(name="locale", nullable=false)
	private byte[] locale;
	
	@Type(type="org.hibernate.spatial.GeometryType")
	@Column(name="location_geometry", columnDefinition="Geometry")
    private Geometry location;
	
	@Column(name="id_local", nullable=true)
	protected Long idLocal;

	public JSONArray getLocaleArray() throws UnsupportedEncodingException {
		String str = new String(this.locale, "UTF-8");
		return new JSONArray("[" + str + "]");
	}
	
	public String getPolygon() throws UnsupportedEncodingException {
		return GeometryUtils.arrayToPolygonStr(getLocaleArray());
	}

	public int compareTo(SDLArea o) {
		if(this.location.getArea() > o.getLocation().getArea()) {
			return -1;
		}
		return 1;
	}

}	
