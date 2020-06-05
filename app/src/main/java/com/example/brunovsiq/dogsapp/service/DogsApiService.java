package com.example.brunovsiq.dogsapp.service;

import com.example.brunovsiq.dogsapp.model.DogBreed;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogsApiService {

    private static final String BASE_URL = "https://raw.githubusercontent.com/";

    private IDogsApi api;

    public DogsApiService() {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(IDogsApi.class);
    }

    public Single<ArrayList<DogBreed>> getDogs() {
        return api.getDogs();
    }

}
