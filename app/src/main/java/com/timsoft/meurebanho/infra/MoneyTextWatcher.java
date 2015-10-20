package com.timsoft.meurebanho.infra;

import android.widget.EditText;

import java.text.NumberFormat;


public class MoneyTextWatcher extends AbstractDecimalTextWatcher {

    public MoneyTextWatcher(EditText editText) {
        super(editText);
    }

    protected NumberFormat getNumberFormat() {
        return NumberFormat.getCurrencyInstance();
    }
}