package com.example.courseprifs.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Restaurant extends BasicUser {
    private List<Cuisine> dishes;
    private String workHours;
    private double rating;

    public Restaurant(String login, String password, String name, String surname, String phoneNumber, String address, List<Cuisine> dishes, String workHours, double rating) {
        super(login, password, name, surname, phoneNumber, address);
        this.dishes = dishes;
        this.workHours = workHours;
        this.rating = rating;
    }
}