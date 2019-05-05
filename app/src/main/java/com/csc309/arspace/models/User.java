package com.csc309.arspace.models;

public class User {
    private String firstName;
    private String lastName;
    private String userID;
    private String email;
    private String password;

    public User(String firstName, String lastName, String userID, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.email = email;
        this.password = password;
    }
}
