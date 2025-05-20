package com.example.InternTask.model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable{
    private String id;
    private String name;
    private String phone;
    private String email;


    public User() {
    }

    public User(String name, String phone, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "User Details:\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Phone: " + phone + "\n" +
                "Email: " + email + "\n";
    }

 
}
