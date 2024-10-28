package com.monsite.model;

public class User {
    private Long id;
    private String lastname;
    private String firstname;
    private String age;
    private String status;
    private String username;
    private String password;

    // Constructeurs
    public User() {}

    public User(String lastname, String firstname, String age, String status, String username, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.age = age;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}