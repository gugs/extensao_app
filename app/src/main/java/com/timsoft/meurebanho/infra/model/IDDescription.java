package com.timsoft.meurebanho.infra.model;

import android.os.Parcel;
import android.os.Parcelable;

public class IDDescription implements Parcelable{
	private int id;
	private String description;
	
	public IDDescription () {
	}
	
	public IDDescription (int id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public IDDescription(Parcel in) {
		String[] data = new String[2];

		in.readStringArray(data);
		setId(Integer.parseInt(data[0]));
		setDescription(data[1]);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descricao) {
		this.description = descricao;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { Integer.toString(getId()),
				getDescription() });
	}
	

	@Override
	public String toString() {
		return "IDDescription [id=" + id + ", description=" + description + "]";
	}
}
