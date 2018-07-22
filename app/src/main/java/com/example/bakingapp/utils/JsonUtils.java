package com.example.bakingapp.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Ingredient;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class JsonUtils{

    private static final String NAME = "name";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";
    private static final String STEPS = "steps";
    private static final String ID = "id";
    private static final String JSONFILENAME = "data.json";
    private static final String INGREDIENTS = "ingredients";
    private static final String MEASURE = "measure";
    private static final String QUANTITY = "quantity";
    private static final String INGREDIENT = "ingredient";
    private static final String STEP_ID = "id";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO = "videoURL";
    private static final String THUMBNAIL = "thumbnailURL";

    public static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open(JSONFILENAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static URL getUrlJson(Context context) {
        try {
            URL jsonUrl = new URL(context.getString(R.string.STRING_URL));
            return jsonUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("tester", "nothing to show");
            return null;
        }
    }

    public static ArrayList<Recipe> parseJsonRecipes(String json){
        if (json != null){
            String id;
            String name;
            ArrayList<Ingredient> ingredients;
            ArrayList<Step> steps;
            String servings;
            String image;

            try{
                ArrayList<Recipe> jsonToRecipe = new ArrayList<>();
                JSONArray allRecipes = new JSONArray(json);
                for (int i=0; i<allRecipes.length(); i++){
                    JSONObject recipe = allRecipes.getJSONObject(i);
                    id = recipe.getString(ID);
                    name = recipe.getString(NAME);
                    ingredients = new ArrayList<>();
                    JSONArray ingredientsJson = recipe.getJSONArray(INGREDIENTS);
                    for(int j=0; j<ingredientsJson.length(); j++){
                        JSONObject ing = ingredientsJson.getJSONObject(j);
                        String measure = ing.getString(MEASURE);
                        String ingredient = ing.getString(INGREDIENT);
                        ingredient = ingredient.substring(0,1).toUpperCase() + ingredient.substring(1);
                        String quantity = ing.getString(QUANTITY);
                        Ingredient ingredientObject = new Ingredient(Float.parseFloat(quantity), measure, ingredient);
                        ingredients.add(ingredientObject);
                    }
                    steps = new ArrayList<>();
                    JSONArray stepsJson = recipe.getJSONArray(STEPS);
                    for(int j=0; j<stepsJson.length(); j++){
                        JSONObject step = stepsJson.getJSONObject(j);
                        String stepId = step.getString(STEP_ID);
                        String shortDescription = step.getString(SHORT_DESCRIPTION);
                        String description = step.getString(DESCRIPTION);
                        String videoURL = step.getString(VIDEO);
                        String thumbnail = step.getString(THUMBNAIL);
                        Step stepObject = new Step(Integer.parseInt(stepId), shortDescription, description, videoURL, thumbnail);
                        steps.add(stepObject);
                    }
                    servings = recipe.getString(SERVINGS);
                    image = recipe.getString(IMAGE);
                    jsonToRecipe.add(new Recipe(id,name,ingredients,steps,servings,image));
                }
                return jsonToRecipe;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else return null;
    }

    public static String getJsonResponseFromUrlRequest (URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}