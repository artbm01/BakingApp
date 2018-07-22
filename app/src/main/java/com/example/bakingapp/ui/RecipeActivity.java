package com.example.bakingapp.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeAdapter;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<Recipe>>{

    ArrayList<Recipe> recipes;
    public static final int LOADER = 22;
    RecipeAdapter listAdapter;
    @BindView(R.id.rv_recipes_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("tester","oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(LOADER, null, this);

        listAdapter = new RecipeAdapter(this, this);

        RecyclerView.LayoutManager layoutManager;
        if(findViewById(R.id.two_pane) != null){
            layoutManager = new GridLayoutManager(this, 3);
        } else{
            layoutManager = new LinearLayoutManager(this);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(Recipe recipe) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.RECIPE_OBJECT), recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<ArrayList<Recipe>>(this) {
            @Nullable
            @Override
            public ArrayList<Recipe> loadInBackground() {
                String json;
                URL jsonUrl = JsonUtils.getUrlJson(RecipeActivity.this);
                try {
                    json = JsonUtils.getJsonResponseFromUrlRequest(jsonUrl);
                    Log.v("tester",json);
                    return JsonUtils.parseJsonRecipes(json);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        recipes = data;
        listAdapter.setAdapterData(data);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<ArrayList<Recipe>> loader) {

    }
}
