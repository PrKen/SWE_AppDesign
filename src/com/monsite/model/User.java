package com.monsite.model;

public class User {
    private long id;  // Updated type to long to handle larger values and match database type
    private String lastname;
    private String firstname;
    private String email;
    private String username;
    private String password;
    private String status;

    public User() {
        // Constructeur par défaut
    }

    public User(String lastname, String firstname, String email, String username, String password, String status) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email ne peut pas être vide");
        }
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}