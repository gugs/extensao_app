package com.timsoft.meurebanho.event.model;

import java.util.Date;

import com.timsoft.meurebanho.infra.model.IDDescricao;

public class Evento extends IDDescricao {
	private Date data;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
