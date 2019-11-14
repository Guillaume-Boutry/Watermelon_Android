package org.boutry.watermelon.rest;

import androidx.annotation.NonNull;

import org.boutry.watermelon.data.model.LoggedInUser;

import java.util.HashMap;
import java.util.Map;

public class LoggedInRequest extends Request {


    public LoggedInRequest(@NonNull LoggedInUser loggedInUser, @NonNull Method method, @NonNull String endPoint) {
        this(loggedInUser, method, endPoint, new HashMap<>());
    }

    public LoggedInRequest(@NonNull LoggedInUser loggedInUser, @NonNull Method method, @NonNull String endPoint, @NonNull Map<String, String> headers) {
        super(method, endPoint, headers);
        this.addAuthToken(loggedInUser.getApiKey());
    }

    private void addAuthToken(@NonNull String token) {
        this.addHeader("x-auth-token", token);
    }

    public static LoggedInRequest get(@NonNull LoggedInUser user, @NonNull String endpoint){
        return new LoggedInRequest(user, Method.GET, endpoint);
    }


}
