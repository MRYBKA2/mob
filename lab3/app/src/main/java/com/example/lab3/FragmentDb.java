package com.example.lab3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentDb extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_db, container, false);

        Button buttonShow = view.findViewById(R.id.button_show);
        Button buttonClear = view.findViewById(R.id.button_clear);
        final MainActivity m = (MainActivity) getActivity();

        buttonShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                m.showTable();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                m.clearTable();
            }
        });

        return view;
    }
}