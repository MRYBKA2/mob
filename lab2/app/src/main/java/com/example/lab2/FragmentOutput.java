package com.example.lab2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentOutput extends Fragment {

    private TextView textViewSelData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_output, container, false);

        textViewSelData = view.findViewById(R.id.textView_sel_data);
        return view;
    }

    public void outputSelectedItems(String author, String year) {
        if (author.equals("")) {
            author = "not selected";
        }

        if (year.equals("")) {
            year = "not selected";
        }

        String result = "Author: " + author + "\n" + "Year: " + year;

        textViewSelData.setText(result);
    }
}