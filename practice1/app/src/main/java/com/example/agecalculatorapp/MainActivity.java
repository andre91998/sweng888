package com.example.agecalculatorapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView firstName = findViewById(R.id.editFirstName);
        TextView lastName = findViewById(R.id.editLastName);
        TextView dateText = findViewById(R.id.setDate);
        Button calculateAgeButton = findViewById(R.id.calculateButton);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN);
        dateFormat.setLenient(false); //for validating the date values are valid

        calculateAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Use the SimpleDateFormat class to parse the date of birth entered by the user into a Date object
                try {
                    Calendar calendar = Calendar.getInstance();
                    Date date = dateFormat.parse(dateText.getText().toString());
                    if (date == null) {
                        showErrorToast(getApplicationContext());
                    }

                    calendar.setTime(date);
                    int year = calendar.getWeekYear();
                    validateYear(year); //validate this is not a future year

                    //Calculate the user's age by subtracting the user's birth year from the current year.
                    Toast ageToast = new Toast(getApplicationContext());
                    ageToast.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - year));

                    //Display the calculated age in a Toast.
                    ageToast.show();
                } catch (ParseException p) {
                    //Show Error Toast
                    showErrorToast(getApplicationContext());
                } catch (InvalidPropertiesFormatException e) {
                    showBadYearToast(getApplicationContext());
                }
            }
        });
    }

    private void showErrorToast(Context context) {
        Toast errorToast = new Toast(context);
        errorToast.setText("INCORRECT DATE FORMAT, PLEASE USE dd-MM-yyyy");
        errorToast.show();
    }

    private void showBadYearToast(Context context) {
        Toast errorToast = new Toast(context);
        errorToast.setText("INVALID YEAR");
        errorToast.show();
    }

    private void validateYear(int year) throws InvalidPropertiesFormatException {
        if (year > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new InvalidPropertiesFormatException("Invalid Year");
        }
    }
}