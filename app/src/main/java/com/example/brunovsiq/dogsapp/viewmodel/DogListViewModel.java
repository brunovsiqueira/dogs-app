package com.example.brunovsiq.dogsapp.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.brunovsiq.dogsapp.model.DogBreed;
import com.example.brunovsiq.dogsapp.model.DogDAO;
import com.example.brunovsiq.dogsapp.model.DogDatabase;
import com.example.brunovsiq.dogsapp.service.DogsApiService;
import com.example.brunovsiq.dogsapp.util.Constants;
import com.example.brunovsiq.dogsapp.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DogListViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<DogBreed>> dogsList = new MutableLiveData<ArrayList<DogBreed>>(); //mutable because we need to change it.
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<Boolean>();

    private DogsApiService dogsService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    private InsertDogsAsyncTask insertDogsAsyncTask;
    private GetDogsAsyncTask getDogsAsyncTask;

    private SharedPreferencesHelper preferencesHelper = SharedPreferencesHelper.getInstance(getApplication());

    public DogListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        long updateTime = preferencesHelper.getUpdateTime();
        long currentTime = System.nanoTime();
        if (updateTime != 0 && currentTime - updateTime < Constants.REFRESH_TIME) {
            fetchFromDatabase();
        } else {
            fetchFromServer();
            preferencesHelper.saveUpdateTime(currentTime);
        }

    }

    public void fetchFromDatabase() {
        isLoading.setValue(true);
        getDogsAsyncTask = new GetDogsAsyncTask();
        getDogsAsyncTask.execute();
    }

    public void fetchFromServer() {
        isLoading.setValue(true);
        disposable.add(
                dogsService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ArrayList<DogBreed>>() {
                            @Override
                            public void onSuccess(ArrayList<DogBreed> dogBreeds) {
                                insertDogsAsyncTask = new InsertDogsAsyncTask();
                                insertDogsAsyncTask.execute(dogBreeds);
                                Toast.makeText(getApplication(), "Dogs retrieved from API", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogLoadError.setValue(true);
                                isLoading.setValue(false);
                            }
                        })
        );
    }

    private void dogsRetrieved(ArrayList<DogBreed> dogBreedList) {
        dogsList.setValue(dogBreedList);
        dogLoadError.setValue(false);
        isLoading.setValue(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if (insertDogsAsyncTask != null) {
            insertDogsAsyncTask.cancel(true);
            insertDogsAsyncTask = null;
        }
        if (getDogsAsyncTask != null) {
            getDogsAsyncTask.cancel(true);
            getDogsAsyncTask = null;
        }
    }

    private class InsertDogsAsyncTask extends AsyncTask<ArrayList<DogBreed>, Void, ArrayList<DogBreed>> {

        @Override
        protected ArrayList<DogBreed> doInBackground(ArrayList<DogBreed>... arrayLists) {
            ArrayList<DogBreed> dogsList = arrayLists[0];
            DogDAO dogDAO = DogDatabase.getInstance(getApplication()).dogDAO();
            dogDAO.deleteAllDogs();

            ArrayList<DogBreed> newList = new ArrayList<>(dogsList);
            List<Long> result = dogDAO.insertAll(newList.toArray(new DogBreed[0]));

            for (int i = 0; i < dogsList.size(); i++) {
                dogsList.get(i).uid = result.get(i).intValue();
            }

            return dogsList;
        }

        @Override
        protected void onPostExecute(ArrayList<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
        }
    }

    private class GetDogsAsyncTask extends AsyncTask<Void, Void, ArrayList<DogBreed>> {


        @Override
        protected ArrayList<DogBreed> doInBackground(Void... voids) {

            DogDAO dogDAO = DogDatabase.getInstance(getApplication()).dogDAO();

            return new ArrayList<>(dogDAO.getAllDogs());
        }

        @Override
        protected void onPostExecute(ArrayList<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "Dogs retrieved from DATABASE", Toast.LENGTH_SHORT).show();
        }
    }

}
