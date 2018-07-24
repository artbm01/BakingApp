package com.example.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class WidgetHelperActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>{


    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ArrayList<Recipe> recipes;
    private static final String PREFERENCES = "com.example.bakingapp.widget.RecipeWidgetProvider";
    private static final String PREFS_KEY = "widget_prefs";
    private Spinner spinner;
    private static final int LOADER = 17;

    public WidgetHelperActivity() {super();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_helper);
        Button button = findViewById(R.id.widget_button);
        button.setOnClickListener(OnClickListener);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            appWidgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        getSupportLoaderManager().initLoader(LOADER, null, this);

    }

    private static void saveTextToPreference(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES, 0).edit();
        prefs.putString(PREFS_KEY + appWidgetId, text);
        prefs.apply();
    }

    static String loadTextFromPreference(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        String titleValue = prefs.getString(PREFS_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTextFromPreference(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES, 0).edit();
        prefs.remove(PREFS_KEY + appWidgetId);
        prefs.apply();
    }

    private final View.OnClickListener OnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetHelperActivity.this;
            int position;

            position = spinner.getSelectedItemPosition();

            String widgetText = recipes.get(position).getName() + "\n\n" + getFormattedIngredients(recipes.get(position).getIngredients());
            saveTextToPreference(context, appWidgetId, widgetText);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    private void setupSpinner(ArrayList<String> arrayList) {
        spinner = findViewById(R.id.widget_spinner);
        @SuppressWarnings("unchecked") ArrayAdapter<String> adapter = new ArrayAdapter(WidgetHelperActivity.this,
                android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<ArrayList<Recipe>>(this) {
            @Nullable
            @Override
            public ArrayList<Recipe> loadInBackground() {
                String json;
                URL jsonUrl = JsonUtils.getUrlJson(WidgetHelperActivity.this);
                try {
                    json = JsonUtils.getJsonResponseFromUrlRequest(jsonUrl);
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
    public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        recipes = data;
        ArrayList<String> recipeNames = new ArrayList<>();
        for (Recipe recipe : data){
            recipeNames.add(recipe.getName());
        }
        setupSpinner(recipeNames);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {

    }

    private String getFormattedIngredients(ArrayList<Ingredient> ingredients){
        String string="";
        for (Ingredient item : ingredients){
            string = string + (item.getIngredient());
            string = string + ("\n");
        }
        string = string.substring(0,string.length()-1);
        return string;
    }
}
