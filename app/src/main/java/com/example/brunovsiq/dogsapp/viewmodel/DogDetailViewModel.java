package com.example.brunovsiq.dogsapp.viewmodel;

import android.app.Application;

import com.example.brunovsiq.dogsapp.model.DogBreed;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class DogDetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dog = new MutableLiveData<DogBreed>();

    public DogDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch() {
        DogBreed dog1 = new DogBreed("1", "Golden", "13 years", "", "companionship", "calm and friendly", "");
        dog.setValue(dog1);

    }



}
