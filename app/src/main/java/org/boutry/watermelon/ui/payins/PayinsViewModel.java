package org.boutry.watermelon.ui.payins;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boutry.watermelon.Constants;
import org.boutry.watermelon.data.LoginRepository;
import org.boutry.watermelon.data.Result;
import org.boutry.watermelon.data.model.NotLoggedInException;
import org.boutry.watermelon.data.model.PayIn;
import org.boutry.watermelon.rest.LoggedInRequest;
import org.boutry.watermelon.rest.Request;
import org.boutry.watermelon.rest.RestAsync;

import java.util.List;

public class PayinsViewModel extends ViewModel {
    private MutableLiveData<List<PayIn>> payins;

    public LiveData<List<PayIn>> getPayins() {
        if (payins == null) {
            payins = new MutableLiveData<>();
            loadPayins();
        }
        return payins;
    }

    private void loadPayins() {
        try {
            Request req = LoggedInRequest.get(LoginRepository.getInstance().getLoggedInUser(), Constants.API + "/payins");
            new RestAsync<>(new Handler(), PayIn::parseListJson, this::handleResult).execute(req);
        }
        catch (NotLoggedInException e) {
            //do nothing
        }
    }

    private void handleResult(@NonNull List<Result<List<PayIn>>> results) {
        for(Result<List<PayIn>> result : results) {
            if (result instanceof Result.Success) {
                payins.setValue(((Result.Success<List<PayIn>>) result).getData());
            }
        }
    }
}
