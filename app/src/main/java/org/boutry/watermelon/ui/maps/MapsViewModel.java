package org.boutry.watermelon.ui.maps;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapsViewModel extends ViewModel {

    private MutableLiveData<Location> location;

    public LiveData<Location> getLocation() {
        if (location == null)
            location = new MutableLiveData<>();
        return location;
    }


    public void setLocation(Location location) {
        this.location.setValue(location);
    }
}