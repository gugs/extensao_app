package com.timsoft.meurebanho.event.model;

import com.timsoft.meurebanho.infra.model.IDDescription;

import java.util.Date;

public class Evento extends IDDescription {
	private Date data;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
