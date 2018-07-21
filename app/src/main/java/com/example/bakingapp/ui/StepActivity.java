package com.example.bakingapp.ui;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private ArrayList<Step> stepsList;
    int id;
    Button nextButton;
    Button previousButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);

        id = getIntent().getIntExtra(getResources().getString(R.string.STEP_ID),0);
        stepsList = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST));

        StepFragment fragment = new StepFragment();
        fragment.setStep(stepsList.get(id).getDescription());
        fragment.setVideoUrl(stepsList.get(id).getVideoUrl());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.step_fragment, fragment).commit();


        if(stepsList.size() == (id+1)){
            nextButton.setVisibility(View.GONE);
        }
        if(id==0){
            previousButton.setVisibility(View.GONE);
        }

    }


}
