package com.timsoft.meurebanho.farm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.timsoft.meurebanho.infra.model.IDDescription;

public class Farm extends IDDescription {
	public Farm(int id, String description) {
		super(id, description);
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
