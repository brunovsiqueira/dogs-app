package com.example.brunovsiq.dogsapp.service;

import com.example.brunovsiq.dogsapp.model.DogBreed;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface IDogsApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    Single<ArrayList<DogBreed>> getDogs();

}
