package com.example.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepActivity extends AppCompatActivity {

    private Recipe recipe;
    private Bundle bundleFromDetail;
    private int id;
    @BindView(R.id.next_button) Button nextButton;
    @BindView(R.id.previous_button) Button previousButton;
    private String videoUrl;
    StepFragment fragment;
    private Bundle bundleForFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);

        Intent intentThatCreatedActivity = getIntent();
        if (intentThatCreatedActivity.hasExtra(getString(R.string.RECIPE_OBJECT))){
            bundleFromDetail = intentThatCreatedActivity.getExtras();
            recipe = bundleFromDetail.getParcelable(getString(R.string.RECIPE_OBJECT));
        }
        if(savedInstanceState != null){
            id = savedInstanceState.getInt(getString(R.string.VIDEO_ID));
        } else{
            if(intentThatCreatedActivity.hasExtra(getString(R.string.STEP_ID))){
                id = intentThatCreatedActivity.getIntExtra(getString(R.string.STEP_ID),0);
            }
        }

        handleActionBar();

        if (savedInstanceState == null) {
            fragment = new StepFragment();
        }

        bundleForFragment = new Bundle();
        bundleForFragment.putInt(getString(R.string.STEP_ID), id);
        bundleForFragment.putParcelableArrayList(getString(R.string.STEPS_LIST),recipe.getSteps());
        fragment.setArguments(bundleForFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.step_fragment, fragment).commit();
        prepareButtons();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commitNow();
        fragment.onStop();
        fragment.onPause();
        bundleForFragment.putInt(getString(R.string.STEP_ID), id);
        fragment.setArguments(bundleForFragment);
        fragmentManager.beginTransaction().add(R.id.step_fragment, fragment).commit();
        prepareButtons();
    }

    private void prepareButtons(){
        if(recipe.getSteps().size()-1 == id){
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
        } if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            previousButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.VIDEO_ID), id);
        super.onSaveInstanceState(outState);
    }

    private void handleActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
