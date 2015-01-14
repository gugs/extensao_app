package com.timsoft.meurebanho.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Fazenda extends IDDescricao {
	public Fazenda(int id, String descricao) {
		super(id, descricao);
	}

	public Fazenda(Parcel in) {
		super(in);
	}
	
	public static final Parcelable.Creator<Fazenda> CREATOR = new Parcelable.Creator<Fazenda>() {
		public Fazenda createFromParcel(Parcel in) {
			return new Fazenda(in);
		}

		public Fazenda[] newArray(int size) {
			return new Fazenda[size];
		}
	};
}
