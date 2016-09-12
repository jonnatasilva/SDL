package br.com.trabalho.tg.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name="historico_area")
public class HistoricoArea {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="data_alteracao", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	@Column(name="local_antes", nullable=true)
	private byte[] localeAntes;
	
	@Column(name="id_usuario", nullable=false)
	private String idUsuario;
}
