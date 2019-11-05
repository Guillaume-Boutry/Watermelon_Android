package org.boutry.watermelon.data.model;

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
}
