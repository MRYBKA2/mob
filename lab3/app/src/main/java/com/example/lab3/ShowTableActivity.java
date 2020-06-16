package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table);

        ArrayList<String> records = getIntent().getStringArrayListExtra("records");
        TextView textViewTable = findViewById(R.id.textView_table);

        StringBuilder result = new StringBuilder();
        if(records.size() > 0) {
            for(String record: records) {
                result.append(record);
            }
        } else {
            String emptyTblMsg = getResources().getString(R.string.table_is_empty_msg);
            result.append(emptyTblMsg);
        }

        textViewTable.setText(result);
    }
}