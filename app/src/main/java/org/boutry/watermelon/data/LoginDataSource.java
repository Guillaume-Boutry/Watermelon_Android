package org.boutry.watermelon.data;

import android.util.JsonReader;

import org.boutry.watermelon.Constants;
import org.boutry.watermelon.data.model.LoggedInUser;
import org.boutry.watermelon.data.model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            User parsedUser = null;
            String apiKey = null;
            int id = -1;
            {
                URL url = new URL(Constants.API + "/login");


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // IN ms
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);

                connection.setRequestMethod("POST");


                connection.setDoInput(true);
                connection.setDoOutput(true);
                String body = String.format("email=%s&password=%s", username, password);
                connection.setRequestProperty("Content-Length", body.length() + "");
                connection.setRequestProperty("charset", "utf-8");
                try (DataOutputStream o = new DataOutputStream(connection.getOutputStream())) {
                    o.write(body.getBytes(StandardCharsets.UTF_8));
                }
                connection.connect();

                try (InputStream inputStream = connection.getInputStream();
                     JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String nextName = reader.nextName();
                        if (nextName.equals("access_token")) {
                            apiKey = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                }
            }
            {
                URL url = new URL(Constants.API + "/users");


                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // IN ms
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-auth-token", apiKey);
                connection.setDoInput(true);
                connection.connect();

                try (InputStream inputStream = connection.getInputStream();
                     JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        parsedUser = User.parseJson(reader);
                    }
                    reader.endArray();
                }
            }

            LoggedInUser user =
                    new LoggedInUser(
                            parsedUser, apiKey);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
