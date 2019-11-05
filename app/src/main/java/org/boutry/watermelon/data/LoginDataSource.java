package org.boutry.watermelon.data;

import org.boutry.watermelon.data.model.LoggedInUser;
import org.boutry.watermelon.data.model.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            new User(1, "Patrick", "Beaux", "patrick.beau@gmail.com"),
                            "7842afdceecde265091634ef");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
