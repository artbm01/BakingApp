package com.example.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private ArrayList<Step> stepsList;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        id = getIntent().getIntExtra(getResources().getString(R.string.STEP_ID),0);
        stepsList = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST));

        StepFragment fragment = new StepFragment();
        fragment.setStep(stepsList.get(id).getDescription());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.step_fragment, fragment).commit();

    }
}
