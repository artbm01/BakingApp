package com.example.bakingapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;

public class StepFragment extends Fragment{

    String descriptionText;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        final TextView description = rootView.findViewById(R.id.step_description_tv);
        description.setText(descriptionText);


        return rootView;
    }

    public void setStep(String descriptionText){
        this.descriptionText = descriptionText;
    }
}
