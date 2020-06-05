package com.example.brunovsiq.dogsapp.model;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DogDAO {

    @Insert
    List<Long> insertAll(DogBreed... dogBreeds); //returns the list of inserted dogs' uids

    @Query("SELECT * FROM dogbreed")
    List <DogBreed> getAllDogs();

    @Query("SELECT * FROM dogbreed WHERE uid = :dogId")
    DogBreed getDogById(int dogId);

    @Query("DELETE FROM dogbreed")
    void deleteAllDogs();

}

