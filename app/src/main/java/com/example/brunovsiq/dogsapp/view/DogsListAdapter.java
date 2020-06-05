package com.example.brunovsiq.dogsapp.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brunovsiq.dogsapp.R;
import com.example.brunovsiq.dogsapp.model.DogBreed;
import com.example.brunovsiq.dogsapp.util.Util;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> {

    private ArrayList<DogBreed> dogsList;
    private Context context;

    public DogsListAdapter(ArrayList<DogBreed> dogsList, Context context) {
        this.dogsList = dogsList;
        this.context = context;
    }

    public void updateDogsList(ArrayList<DogBreed> newDogsList) {
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        DogBreed dog = dogsList.get(position);
        holder.name.setText(dog.dogBreed);
        holder.lifespan.setText(dog.lifeSpan);
        Util.loadImage(holder.dogImage, dog.imageUrl, Util.getProgressDrawable(holder.dogImage.getContext()));
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        public ImageView dogImage;
        public TextView name;
        public TextView lifespan;

        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            dogImage = itemView.findViewById(R.id.item_dog_image);
            name = itemView.findViewById(R.id.item_dog_name);
            lifespan = itemView.findViewById(R.id.item_dog_lifespan);

            itemView.setOnClickListener(v -> {
                NavDirections navDirection = DogListFragmentDirections.detailAction(5); //TODO: pass correct dog id
                Navigation.findNavController(itemView).navigate(navDirection);
            });
        }
    }
}
