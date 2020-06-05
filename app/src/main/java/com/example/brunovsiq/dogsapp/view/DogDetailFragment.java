package com.example.brunovsiq.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brunovsiq.dogsapp.R;
import com.example.brunovsiq.dogsapp.model.DogBreed;
import com.example.brunovsiq.dogsapp.viewmodel.DogDetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DogDetailFragment extends Fragment {

    @BindView(R.id.detail_name)
    TextView name;

    @BindView(R.id.detail_purpose)
    TextView purpose;

    @BindView(R.id.detail_lifespan)
    TextView lifespan;

    @BindView(R.id.detail_temperament)
    TextView temperament;

    @BindView(R.id.detail_image)
    ImageView dogImage;

    private DogDetailViewModel dogDetailViewModel;

    public DogDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dogDetailViewModel = ViewModelProviders.of(this).get(DogDetailViewModel.class);
        dogDetailViewModel.fetch();

        if (getArguments() != null) {
            int dogId = getArguments().getInt("dogId");
        }
        observeViewModel();

    }

    private void observeViewModel() {
        dogDetailViewModel.dog.observe(this, dog -> {
            if (dog != null) {
                name.setText(dog.dogBreed);
                purpose.setText(dog.bredFor);
                lifespan.setText(dog.lifeSpan);
                temperament.setText(dog.temperament);

            }
        });
    }


}
