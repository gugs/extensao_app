package com.timsoft.meurebanho.model;

public class IDDescricao {
	private long id;
	private String descricao;
	
	public IDDescricao () {
	}
	
	public IDDescricao (long id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "IDDescricao [id=" + id + ", descricao=" + descricao + "]";
	}
}
