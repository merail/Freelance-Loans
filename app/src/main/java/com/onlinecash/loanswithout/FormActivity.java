package com.onlinecash.loanswithout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class FormActivity extends AppCompatActivity {

    private ArrayList<TextInputLayout> editTexts;
    private ArrayList<String> errorEmptyMessages;
    private ArrayList<String> errorMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        LinearLayout backLinearLayout = findViewById(R.id.backLinearLayout);
        backLinearLayout.setOnClickListener(view -> onBackPressed());

        editTexts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String iconId = "editText" + i;
            int resIconId = getResources().getIdentifier(iconId, "id", this.getPackageName());

            editTexts.add(findViewById(resIconId));
        }

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            Objects.requireNonNull(editTexts.get(i).getEditText()).setOnFocusChangeListener((view, b) -> {
                if (!b) {
                    if (Objects.requireNonNull(editTexts.get(finalI).getEditText()).getText().toString().trim().isEmpty()) {
                        editTexts.get(finalI).setError(errorEmptyMessages.get(finalI));
                    } else {
                        editTexts.get(finalI).setErrorEnabled(false);
                    }
                } else {
                    editTexts.get(finalI).setErrorEnabled(false);
                }
            });
        }

        errorEmptyMessages = new ArrayList<>();
        errorEmptyMessages.add("Необходимо ввести Ф.И.О");
        errorEmptyMessages.add("Необходимо ввести номер телефона");
        errorEmptyMessages.add("Необходимо ввести адрес эл. почты");
        errorEmptyMessages.add("Необходимо ввести адрес пароль");

        errorMessages = new ArrayList<>();
        errorMessages.add("Используйте кириллицу для ввода имени");
        errorMessages.add("Неверный формат телефонного номера");
        errorMessages.add("Неверный формат почты");

        ImageButton applyImageButton = findViewById(R.id.applyImageButton);
        applyImageButton.setOnClickListener(view -> {
            boolean hasError = false;

//            if (!Objects.requireNonNull(editTexts.get(0).getEditText()).getText().toString().trim().matches("^[а-яА-Я ]+$"))
//            {
//                hasError = true;
//                editTexts.get(0).setError(errorMessages.get(0));
//            }
//            if (Objects.requireNonNull(editTexts.get(1).getEditText()).getText().toString().trim().length() != 10)
//            {
//                hasError = true;
//                editTexts.get(1).setError(errorMessages.get(1));
//            }
//            if (!Objects.requireNonNull(editTexts.get(2).getEditText()).getText().toString().trim().contains("@")
//                    && !Objects.requireNonNull(editTexts.get(2).getEditText()).getText().toString().trim().contains("."))
//            {
//                hasError = true;
//                editTexts.get(2).setError(errorMessages.get(2));
//            }
//            for (int i = 0; i < 4; i++) {
//                if (Objects.requireNonNull(editTexts.get(i).getEditText()).getText().toString().trim().isEmpty()) {
//                    if (!hasError)
//                        hasError = true;
//                    editTexts.get(i).setError(errorEmptyMessages.get(i));
//                }
//            }
            if (!hasError) {
                startActivity(new Intent(FormActivity.this, ProcessingActivity.class));
            }
        });

        CheckBox personalDataCheckBox = findViewById(R.id.personalDataCheckbox);
        personalDataCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                applyImageButton.setVisibility(View.VISIBLE);
            else
                applyImageButton.setVisibility(View.INVISIBLE);
        });
    }
}