package com.example.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.example.bakingapp.R;
import com.example.bakingapp.adapters.DetailAdapter;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailAdapter.ListItemClickListener{

    private Boolean twoPane=false;
    private Recipe recipe;
    @BindView(R.id.detail_ingredients_tv) TextView ingredientTextView;
    @BindView(R.id.rv_details_list) RecyclerView recyclerView;
    private int stepId=0;
    private StepFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.RECIPE_OBJECT))){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                recipe = bundle.getParcelable(getString(R.string.RECIPE_OBJECT));
                handleActionBar();
                setIngredients();
                setSteps();
            }
        }

        if (findViewById(R.id.details_frame_layout) != null){
            twoPane = true;
            fragment = new StepFragment();
            Bundle bundleForFragment = new Bundle();
            bundleForFragment.putInt(getString(R.string.STEP_ID), stepId);
            bundleForFragment.putParcelableArrayList(getString(R.string.STEPS_LIST),recipe.getSteps());
            fragment.setArguments(bundleForFragment);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.details_frame_layout, fragment).commit();
        }
    }

    private void setIngredients(){
        StringBuilder tv = new StringBuilder();
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        for (int i=0; i<ingredients.size(); i++){
            String ingredient = "  - "+ingredients.get(i).getIngredient() + ": "
                    + "(" + ingredients.get(i).getQuantity() + " "
                    + ingredients.get(i).getMeasure() + ")" + "\n";
            tv.append(ingredient);
        }
        ingredientTextView.setText(tv.toString());
    }

    private void setSteps(){
        DetailAdapter adapter = new DetailAdapter(this);
        adapter.setAdapterData(recipe.getSteps());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int id) {
        if(!twoPane){
            Intent intentToEnterStep = new Intent(this,StepActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.RECIPE_OBJECT), recipe);
            intentToEnterStep.putExtras(b);
            intentToEnterStep.putExtra(getString(R.string.STEP_ID), id);
            startActivity(intentToEnterStep);
        }
        else {
            StepFragment newFragment = new StepFragment();
            this.stepId = id;
            Bundle bundleForFragment = new Bundle();
            bundleForFragment.putInt(getString(R.string.STEP_ID), stepId);
            bundleForFragment.putParcelableArrayList(getString(R.string.STEPS_LIST),recipe.getSteps());
            newFragment.setArguments(bundleForFragment);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.details_frame_layout, newFragment).commit();
        }
    }

    private void handleActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
