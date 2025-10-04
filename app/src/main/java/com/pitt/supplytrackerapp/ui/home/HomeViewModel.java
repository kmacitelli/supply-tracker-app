package com.pitt.supplytrackerapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import com.pitt.supplytrackerapp.*;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Bin>> binsLiveData = new MutableLiveData<>();

    public void setBins(List<Bin> bins) {
        binsLiveData.setValue(bins);
    }

    public LiveData<List<Bin>> getBins() {
        return binsLiveData;
    }

}
