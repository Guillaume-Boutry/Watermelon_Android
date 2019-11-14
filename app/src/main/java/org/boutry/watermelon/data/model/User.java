package org.boutry.watermelon.data.model;

import android.util.JsonReader;

import java.io.IOException;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;


    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return getLastName() + " " + getFirstName();
    }

    public static User parseJson(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        int id = 0;
        String firstName = null;
        String lastName = null;
        String email = null;
        try {
            while (jsonReader.hasNext()) {
                final String key = jsonReader.nextName();
                switch (key) {
                    case "id":
                        id = jsonReader.nextInt();
                        break;
                    case "email":
                        email = jsonReader.nextString();
                        break;
                    case "first_name":
                        firstName = jsonReader.nextString();
                        break;
                    case "last_name":
                        lastName = jsonReader.nextString();
                        break;
                    default:
                        jsonReader.skipValue();
                        break;
                }
            }
        } finally {
            jsonReader.endObject();
        }

        return new User(id, firstName, lastName, email);
    }

}
