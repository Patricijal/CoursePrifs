package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    private List<FoodOrder> orders;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Allergens allergens;
    private double price;

    public Cuisine(String title, String description, Allergens allergens, double price) {
        this.title = title;
        this.description = description;
        this.allergens = allergens;
        this.price = price;
    }

    @Override
    public String toString() { return "Title: " + title + " | Description: " + description + " | Allergens: " + allergens + " | Price: " + price; }
}
