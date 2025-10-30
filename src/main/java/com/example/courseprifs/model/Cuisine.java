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
    private String title;
    private String description;
    private Double price;

    @ManyToMany(mappedBy = "food", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodOrder> orders;
    @Enumerated(EnumType.STRING)
    private Allergens allergens;
    private boolean spicy = false;
    private boolean vegan = false;
    @ManyToOne
    private Restaurant restaurant;

    public Cuisine(String title, String description, Allergens allergens, double price) {
        this.title = title;
        this.description = description;
        this.allergens = allergens;
        this.price = price;
    }

    public Cuisine(String title, String description, Double price, boolean spicy, boolean vegan, Restaurant restaurant) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.spicy = spicy;
        this.vegan = vegan;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() { return "Title: " + title + " | Description: " + description + " | Allergens: " + allergens + " | Price: " + price; }
}
