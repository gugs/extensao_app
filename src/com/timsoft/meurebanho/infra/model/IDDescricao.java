package com.timsoft.meurebanho.infra.model;

import android.os.Parcel;
import android.os.Parcelable;

public class IDDescricao implements Parcelable{
	private int id;
	private String descricao;
	
	public IDDescricao () {
	}
	
	public IDDescricao (int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	
	public IDDescricao(Parcel in) {
		String[] data = new String[2];

		in.readStringArray(data);
		setId(Integer.parseInt(data[0]));
		setDescricao(data[1]);
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
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { Integer.toString(getId()),
				getDescricao() });
	}

	@Override
	public String toString() {
		return "IDDescricao [id=" + id + ", descricao=" + descricao + "]";
	}
}
