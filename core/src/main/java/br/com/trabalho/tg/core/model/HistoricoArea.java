package br.com.trabalho.tg.core.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name="sdl_historico_area")
public class HistoricoArea {
	
	public HistoricoArea(byte[] locale, Long idUsuario, SDLArea area) {
		this.dataAlteracao = new Date();
		this.locale = locale;
		this.idUsuario = idUsuario;
		this.area = area;
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="data_alteracao", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	@Lob
	@Column(name="locale", nullable=true)
	private byte[] locale;
	
	@Column(name="id_usuario", nullable=false)
	private Long idUsuario;
	
	@OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="id_area", nullable=true)
	private SDLArea area;
}
