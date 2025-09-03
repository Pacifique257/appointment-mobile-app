package com.example.test.models;

public class RegisterRequest {
    private String lastName;
    private String firstName;
    private String email;
    private String role;
    private String phone;
    private String birthDate;
    private String address;
    private String gender;
    private String password;

    public RegisterRequest(String lastName, String firstName, String email, String role, String phone,
                           String birthDate, String address, String gender, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.gender = gender;
        this.password = password;
    }
}