package org.boutry.watermelon.ui.home;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.boutry.watermelon.Constants;
import org.boutry.watermelon.R;
import org.boutry.watermelon.data.LoginRepository;
import org.boutry.watermelon.data.Result;
import org.boutry.watermelon.data.model.NotLoggedInException;
import org.boutry.watermelon.data.model.PayIn;
import org.boutry.watermelon.data.model.Wallet;
import org.boutry.watermelon.rest.LoggedInRequest;
import org.boutry.watermelon.rest.Request;
import org.boutry.watermelon.rest.RestAsync;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Wallet> wallet;


    public LiveData<Wallet> getBalance() {
        if (wallet == null) {
            wallet = new MutableLiveData<>();
            loadWallet();
        }

        return wallet;
    }

    public void setBalance(Wallet wallet) {
        this.wallet.setValue(wallet);
    }

    private void loadWallet() {
        try {
            Request req = LoggedInRequest.get(LoginRepository.getInstance().getLoggedInUser(), Constants.API + "/wallets");
            new RestAsync<>(new Handler(), Wallet::parseJson, this::handleResult).execute(req);
        }
        catch (NotLoggedInException e) {
            //do nothing
        }
    }

    private void handleResult(@NonNull List<Result<Wallet>> results) {
        for(Result<Wallet> result : results) {
            if (result instanceof Result.Success) {
                wallet.setValue(((Result.Success<Wallet>) result).getData());
            }
        }
    }
}