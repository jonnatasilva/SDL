package br.com.simova.testes.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.simova.testes.vo.AreaMock;

public class AreaDAOMock {
	
	private List<AreaMock> areas = new ArrayList<AreaMock>();
	
	public AreaDAOMock() {
		this.areas.add(new AreaMock("1", "Area 1", "2545452"));
		this.areas.add(new AreaMock("2", "Area 2", "5466566"));
		this.areas.add(new AreaMock("3", "Area 3", "5466656"));
		this.areas.add(new AreaMock("4", "Area 4", "2554543"));
	}
	
	public AreaMock findByCodigo(String codigo) {
		for (AreaMock a : areas) {
			if(a.getCodigo().equals(codigo)) {
				return a;
			}
		}
		return null;
	}
	
	public AreaMock findByDescricao(String descricao) {
		for (AreaMock a : areas) {
			if(a.getDescricao().equals(descricao)) {
				return a;
			}
		}
		return null;
	}
	
	public void save (AreaMock a) {
		this.areas.add(a);
	}

}
