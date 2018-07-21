package com.example.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.models.Step;
import com.example.bakingapp.R;


import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder>{

    private ArrayList<Step> steps;
    private final ListItemClickListener listItemClickListener;

    public DetailAdapter(ListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_template, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAdapter.MyViewHolder holder, int position) {
        holder.recipeStep.setText(steps.get(position).getShortDescription());
        holder.recipeNumber.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView recipeStep;
        private final TextView recipeNumber;

        private MyViewHolder(View itemView) {
            super(itemView);
            recipeStep = itemView.findViewById(R.id.rv_step_tv);
            recipeNumber = itemView.findViewById(R.id.rv_step_number_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemClickListener.onListItemClick(position);
        }
    }

    public interface ListItemClickListener{
        void onListItemClick(int id);
    }

    public void setAdapterData(ArrayList<Step> steps){
        this.steps = steps;
    }
}
