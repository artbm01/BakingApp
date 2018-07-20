package com.example.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private ArrayList<Step> stepsList;
    private TextView description;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        description = findViewById(R.id.step_description_tv);

        id = getIntent().getIntExtra(getResources().getString(R.string.STEP_ID),0);
        stepsList = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST));

        description.setText(stepsList.get(id).getDescription());

    }
}
