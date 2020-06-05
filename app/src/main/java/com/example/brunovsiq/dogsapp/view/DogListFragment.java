package com.example.brunovsiq.dogsapp.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.brunovsiq.dogsapp.R;
import com.example.brunovsiq.dogsapp.model.DogBreed;
import com.example.brunovsiq.dogsapp.viewmodel.DogListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class DogListFragment extends Fragment {

//    private FloatingActionButton fab;
    private RecyclerView dogsRecyclerView;
    private ProgressBar progressBar;
    private TextView textError;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DogListViewModel viewModel;
    private DogsListAdapter dogsListAdapter = new DogsListAdapter(new ArrayList<>(), getContext());

    public DogListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViewItems(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        NavDirections navDirection = DogListFragmentDirections.detailAction(5); //TODO: pass correct dog id
//        Navigation.findNavController(view).navigate(navDirection);

        viewModel = ViewModelProviders.of(this).get(DogListViewModel.class);
        viewModel.refresh();

        dogsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dogsRecyclerView.setAdapter(dogsListAdapter);
        observeViewModel();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.fetchFromServer();
            }
        });

//        fab.setOnClickListener(v -> {
//            onGoToDetails();
//        });
    }

    private void findViewItems(View view) {
        dogsRecyclerView = view.findViewById(R.id.dogs_recyclerview);
        progressBar = view.findViewById(R.id.list_progressbar);
        textError = view.findViewById(R.id.list_text_error);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
//        fab = view.findViewById(R.id.list_floatingActionButton);
    }

    private void observeViewModel() {
        viewModel.dogsList.observe(this, dogs -> {
            if (dogs.size() > 0) {
                dogsRecyclerView.setVisibility(View.VISIBLE);
                dogsListAdapter.updateDogsList(dogs);
            }
        });

        viewModel.isLoading.observe(this, loading -> {
            progressBar.setVisibility(loading? View.VISIBLE : View.INVISIBLE);
            if (loading) {
                textError.setVisibility(View.GONE);
                dogsRecyclerView.setVisibility(View.GONE);
            }
        });

        viewModel.dogLoadError.observe(this, loadError -> {
            textError.setVisibility(loadError ? View.VISIBLE : View.INVISIBLE);
        });
    }

//    private void onGoToDetails() {
//        NavDirections navDirection = ListFragmentDirections.detailAction(5); //TODO: pass correct dog id
//        Navigation.findNavController(fab).navigate(navDirection);
//    }

}
