package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Change_city extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city);

        final EditText editTextField = findViewById(R.id.city_Name);
        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        editTextField.setOnEditorActionListener((v, actionId, event) -> {
            String newCity = editTextField.getText().toString();
            Intent newCityIntent = new Intent(Change_city.this,MainActivity.class);
            newCityIntent.putExtra("City",newCity);
            startActivity(newCityIntent);
        return false;
        });
    }
}