package com.example.ludvig.sens;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class TextValidator implements TextWatcher {
    boolean valid = false;

    private final List<EditText> editTexts;
    private final Button button;

    public TextValidator(ArrayList<EditText> editTexts, Button button) {
        this.editTexts = editTexts;
        this.button = button;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    public void afterTextChanged(Editable s) {
        valid = true;
        for (EditText editText: editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                valid = false;
            }
        }

        button.setEnabled(valid);
    }
}