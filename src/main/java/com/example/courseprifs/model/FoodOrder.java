package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Double price;
    @ManyToOne
    private BasicUser buyer;
    @ManyToMany
    private List<Cuisine> food;
    @ManyToOne
    private Driver driver;
    @OneToOne
    private Chat chat;
    @ManyToOne
    private Restaurant restaurant;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public FoodOrder(String name, Double price, BasicUser buyer, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.restaurant = restaurant;
    }

    public FoodOrder(String name, Double price, BasicUser buyer, List<Cuisine> food, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.food = food;
        this.restaurant = restaurant;
        this.orderStatus = OrderStatus.PENDING;
    }

    @Override
    public String toString() {
        return name + ", " + price + " EUR";
    }
}
