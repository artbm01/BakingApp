package com.example.bakingapp.ui;

import android.content.Intent;
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

    private Boolean twoPane;
    Recipe recipe;
    TextView ingredientTextView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ingredientTextView = findViewById(R.id.detail_ingredients_tv);
        recyclerView = findViewById(R.id.rv_details_list);
        if (findViewById(R.id.details_frame_layout) != null){
            twoPane = true;
        }

        Intent intent = getIntent();
        if(intent.hasExtra(getResources().getString(R.string.RECIPE_OBJECT))){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                recipe = bundle.getParcelable(getResources().getString(R.string.RECIPE_OBJECT));
                setIngredients();
                setSteps();
            }
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
        /*Intent intentToEnterStep = new Intent(this,StepActivity.class);
        intentToEnterStep.putParcelableArrayListExtra(getResources().getString(R.string.STEPS_LIST), recipe.getSteps());
        intentToEnterStep.putExtra(getResources().getString(R.string.STEP_ID), id);
        startActivity(intentToEnterStep);*/
    }
}
