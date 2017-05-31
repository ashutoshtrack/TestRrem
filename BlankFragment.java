package com.example.ashutosh.testrreminderui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ashutosh on 5/31/2017.
 */

public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bulava(v);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    public void bulava(View view){
        Toast.makeText(view.getContext(), "Hellow manh", Toast.LENGTH_SHORT).show();
    }


}
