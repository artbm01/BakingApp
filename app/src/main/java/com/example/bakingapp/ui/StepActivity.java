package com.example.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepActivity extends AppCompatActivity {

    private ArrayList<Step> stepsList;
    int id;
    Button nextButton;
    Button previousButton;
    String videoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);

        if(savedInstanceState != null){
            id = savedInstanceState.getInt(getString(R.string.VIDEO_ID));
        } else{
            id = getIntent().getIntExtra(getResources().getString(R.string.STEP_ID),0);
        }
        stepsList = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST));

        StepFragment fragment = new StepFragment();
        fragment.setStep(stepsList.get(id).getDescription());
        videoUrl = stepsList.get(id).getVideoUrl();
        //fragment.setVideoUrl(stepsList.get(id).getVideoUrl());
        Bundle b = shareVideoUrl();
        fragment.setArguments(b);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.step_fragment, fragment).commit();

        prepareButtons();

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            hideButtons();
            getSupportActionBar().hide();
        }
    }

    public void OnClickNext(View v){
        id = id + 1;
        refreshView();
    }
    public void OnClickPrevious(View v){
        id = id - 1;
        refreshView();
    }

    private void refreshView(){
        StepFragment newFragment = new StepFragment();
        newFragment.setStep(stepsList.get(id).getDescription());
        videoUrl = stepsList.get(id).getVideoUrl();
        Bundle b = shareVideoUrl();
        newFragment.setArguments(b);
        //newFragment.setVideoUrl(videoUrl);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.step_fragment, newFragment).commit();
        prepareButtons();
    }

    private void prepareButtons(){
        if(stepsList.size()-1 == id){
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.VISIBLE);
        }
        else if(id==0){
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
        else{
            nextButton.setVisibility(View.VISIBLE);
            previousButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideButtons(){
        previousButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
    }

    public Bundle shareVideoUrl(){
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.VIDEO_URL),videoUrl);
        return bundle;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.VIDEO_ID), id);
        super.onSaveInstanceState(outState);
    }
}
