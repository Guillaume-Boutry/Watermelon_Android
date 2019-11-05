package org.boutry.watermelon.ui.login;

import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import org.boutry.watermelon.R;
import org.boutry.watermelon.data.LoginRepository;
import org.boutry.watermelon.data.Result;
import org.boutry.watermelon.data.model.LoggedInUser;

import java.util.Map;

public class LoginTask extends AsyncTask<Void, Void, Result<LoggedInUser>> {
    private LoginRepository loginRepository;
    private MutableLiveData<LoginResult> loginResult;
    private Handler handler;
    private String username;
    private String password;

    public LoginTask(LoginRepository loginRepository, MutableLiveData<LoginResult> loginResult, Handler handler, String username, String password) {
        this.loginRepository = loginRepository;
        this.loginResult = loginResult;
        this.handler = handler;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Result<LoggedInUser> doInBackground(Void... naught) {
        return loginRepository.login(username, password);
    }

    @Override
    protected void onPostExecute(Result<LoggedInUser> result) {
        super.onPostExecute(result);
        handler.post(() -> {
            if (result instanceof Result.Success) {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUser().getDisplayName())));
            } else {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });

    }
}
