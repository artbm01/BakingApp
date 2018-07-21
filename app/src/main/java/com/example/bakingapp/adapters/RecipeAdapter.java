package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Recipe;
import com.example.bakingapp.utils.JsonUtils;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder>{

    private final ArrayList<Recipe> recipes;
    private final ListItemClickListener listItemClickListener;

    public RecipeAdapter(Context context, ListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
        String jsonString = JsonUtils.loadJSONFromAsset(context);
        recipes = JsonUtils.parseJsonRecipes(jsonString);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView recipeTitle;
        private final TextView quantity;

        private MyViewHolder(View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.rv_recipe_name_tv);
            quantity = itemView.findViewById(R.id.rv_servings_quantity_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemClickListener.onListItemClick(recipes.get(position));
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_template, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recipeTitle.setText(recipes.get(position).getName());
        holder.quantity.setText(recipes.get(position).getServings());
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        else return recipes.size();
    }

    public interface ListItemClickListener{
        void onListItemClick(Recipe recipe);
    }
}

