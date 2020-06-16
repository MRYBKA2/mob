package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner authorSpinner;
    private RadioGroup yearRadioGroup;
    private TextView textViewSelData;

    public void outputSelectedItems(String author, String year) {
        String warningMsg = "not selected";
        if (author.equals("")) {
            author = warningMsg;
        }

        if (year.equals("")) {
            year = warningMsg;
        }

        String result = "Author: " + author + "\n" + "Year: " + year;

        textViewSelData.setText(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] authors = getResources().getStringArray(R.array.authors);
        final String[] years = getResources().getStringArray(R.array.years);

        authorSpinner = findViewById(R.id.authors);
        yearRadioGroup = findViewById(R.id.years);
        textViewSelData = findViewById(R.id.textView_sel_data);
        Button buttonOK = findViewById(R.id.button_ok);

        ArrayAdapter<String> authorAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, authors);
        authorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        authorSpinner.setAdapter(authorAdapter);

        for (String year : years) {
            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setText(year);
            yearRadioGroup.addView(radioButtonView);
        }

        buttonOK.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String selAuthor = authorSpinner.getSelectedItem().toString();
                String selYear = "";

                int selYearID = yearRadioGroup.getCheckedRadioButtonId();
                if(selYearID != -1) {
                    RadioButton selectedRadioButton = yearRadioGroup.findViewById(selYearID);
                    if (selectedRadioButton != null) {
                        selYear = selectedRadioButton.getText().toString();
                    }
                }

                if (selAuthor.equals("") || selYear.equals("")) {
                    String msg = getResources().getString(R.string.fill_all_fields_msg);
                    Toast toast = Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    authorSpinner.setSelection(0);
                    yearRadioGroup.check(-1);
                    outputSelectedItems(selAuthor, selYear);
                }
            }
        });
    }
}