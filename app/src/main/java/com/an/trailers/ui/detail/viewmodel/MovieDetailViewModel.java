package com.an.trailers.ui.detail.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.data.local.dao.MovieDao;
import com.an.trailers.data.local.entity.MovieEntity;
import com.an.trailers.data.remote.api.MovieApiService;
import com.an.trailers.data.repository.MovieRepository;
import com.an.trailers.ui.base.BaseViewModel;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailViewModel extends BaseViewModel {

    @Inject
    MovieDao movieDao;

    @Inject
    MovieApiService movieApiService;

    private MovieRepository movieRepository;

    private MutableLiveData<MovieEntity> movieDetailsLiveData = new MutableLiveData<>();

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        ((AppController) application).getApiComponent().inject(this);
        movieRepository = new MovieRepository(movieDao, movieApiService);
    }

    public void fetchMovieDetail(MovieEntity movieEntity) {
        movieRepository.fetchMovieDetails(movieEntity.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(resource -> {
            if(resource.isLoaded()) getMovieDetailsLiveData().postValue(resource.data);
        });
    }

    public MutableLiveData<MovieEntity> getMovieDetailsLiveData() {
        return movieDetailsLiveData;
    }
}
