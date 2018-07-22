package com.example.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepActivity extends AppCompatActivity {

    private ArrayList<Step> stepsList;
    private int id;
    @BindView(R.id.next_button) Button nextButton;
    @BindView(R.id.previous_button) Button previousButton;
    private String videoUrl;
    private String name;
    StepFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        name = getIntent().getStringExtra(getString(R.string.NAME));
        handleActionBar();

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            id = savedInstanceState.getInt(getString(R.string.VIDEO_ID));
        } else{
            id = getIntent().getIntExtra(getResources().getString(R.string.STEP_ID),0);
        }
        stepsList = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST));

        if (savedInstanceState == null) {
            fragment = new StepFragment();
        } else {
            // do nothing - fragment is recreated automatically
        }
        fragment.setStep(stepsList.get(id).getDescription());
        videoUrl = stepsList.get(id).getVideoUrl();
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

    private Bundle shareVideoUrl(){
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.VIDEO_URL),videoUrl);
        return bundle;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.VIDEO_ID), id);
        super.onSaveInstanceState(outState);
    }

    private void handleActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
