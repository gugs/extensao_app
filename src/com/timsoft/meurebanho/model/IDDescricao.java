package com.timsoft.meurebanho.model;

public class IDDescricao {
	private int id;
	private String descricao;
	
	public IDDescricao () {
	}
	
	public IDDescricao (int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
