package org.boutry.watermelon.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private User user;
    private String apiKey;

    public LoggedInUser(User user, String apiKey) {
        this.user = user;
        this.apiKey = apiKey;
    }

    public User getUser() {
        return user;
    }

    public String getApiKey() {
        return apiKey;
    }
}
