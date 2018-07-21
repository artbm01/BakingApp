package com.example.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.DetailAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailAdapter.ListItemClickListener{

    private Boolean twoPane=false;
    Recipe recipe;
    TextView ingredientTextView;
    RecyclerView recyclerView;
    int stepId=0;
    StepFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ingredientTextView = findViewById(R.id.detail_ingredients_tv);
        recyclerView = findViewById(R.id.rv_details_list);

        Intent intent = getIntent();
        if(intent.hasExtra(getResources().getString(R.string.RECIPE_OBJECT))){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                recipe = bundle.getParcelable(getResources().getString(R.string.RECIPE_OBJECT));
                setIngredients();
                setSteps();
            }
        }

        if (findViewById(R.id.details_frame_layout) != null){
            twoPane = true;
            fragment = new StepFragment();
            String description = recipe.getSteps().get(stepId).getDescription();
            fragment.setStep(description);
            fragment.setVideoUrl(recipe.getSteps().get(stepId).getVideoUrl());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.details_frame_layout, fragment).commit();
        }


    }

    private void setIngredients(){
        String tv = "Ingredients \n\n";
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        for (int i=0; i<ingredients.size(); i++){
            String ingredient = ingredients.get(i).getIngredient() + ": " + ingredients.get(i).getQuantity() + " " + ingredients.get(i).getMeasure() + "\n";
            tv = tv + ingredient;
        }
        tv += "\nSteps\n";
        ingredientTextView.setText(tv);
    }

    private void setSteps(){
        DetailAdapter adapter = new DetailAdapter(this);
        adapter.setAdapterData(recipe.getSteps());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //HAs to be different for two panel
    @Override
    public void onListItemClick(int id) {
        if(!twoPane){
            Intent intentToEnterStep = new Intent(this,StepActivity.class);
            intentToEnterStep.putParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST), recipe.getSteps());
            intentToEnterStep.putExtra(getResources().getString(R.string.STEP_ID), id);
            startActivity(intentToEnterStep);
        }
        else {
            this.stepId = id;
            String description = recipe.getSteps().get(stepId).getDescription();
            StepFragment newFragment = new StepFragment();
            newFragment.setStep(description);
            newFragment.setVideoUrl(recipe.getSteps().get(stepId).getVideoUrl());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.details_frame_layout, newFragment).commit();
        }
    }

    public void setFrame(){
        String description = recipe.getSteps().get(stepId).getDescription();
        fragment.setStep(description);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.details_frame_layout, fragment).commit();
    }
}
