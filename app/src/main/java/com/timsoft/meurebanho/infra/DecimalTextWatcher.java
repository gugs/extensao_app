package com.timsoft.meurebanho.infra;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class DecimalTextWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;

    public DecimalTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        editText.removeTextChangedListener(this);
        String cleanString = s.toString().replaceAll("[^\\d]", "");
        if (!TextUtils.isEmpty(cleanString)) {
            BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
            String formatted = getNumberFormat().format(parsed);
            editText.setText(formatted);
            editText.setSelection(formatted.length());
        }
        editText.addTextChangedListener(this);
    }

    private NumberFormat getNumberFormat() {
        return NumberFormat.getNumberInstance();
    }
}
