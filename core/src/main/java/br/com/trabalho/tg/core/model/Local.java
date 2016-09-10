package br.com.trabalho.tg.core.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Local {
	
	private Long id;
	private String codigo; 
	private String descricao;
	private String timezone; 
	private List<AreaLocal> areasLocal = new ArrayList<AreaLocal>();
	
	public Local(Long id, String codigo, String descricao, String timezone, List<?> areas) throws Exception {
		this.id = id;
		this.codigo = codigo;
		this.descricao = descricao;
		this.timezone = timezone;
		this.parseToListAreaLocal(areas);
	}
	
	public void parseToListAreaLocal(List<?> areasObject) throws Exception {
		
		if(areasObject != null) {
			for(Object o : areasObject) {
				
				//Invocar metódos do objeto
				Method getCodigo = o.getClass().getMethod("getCodigo");
				Method getDescricao = o.getClass().getMethod("getDescricao");
				
				AreaLocal areaLocal = new AreaLocal(getCodigo.invoke(o).toString(),
						getDescricao.invoke(o).toString());
				
				areasLocal.add(areaLocal);
			}
		}
	}
}
