package com.timsoft.meurebanho.farm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.timsoft.meurebanho.infra.model.IDDescricao;

public class Farm extends IDDescricao {
	public Farm(int id, String descricao) {
		super(id, descricao);
	}

	public Farm(Parcel in) {
		super(in);
	}
	
	public static final Parcelable.Creator<Farm> CREATOR = new Parcelable.Creator<Farm>() {
		public Farm createFromParcel(Parcel in) {
			return new Farm(in);
		}

		public Farm[] newArray(int size) {
			return new Farm[size];
		}
	};
}
