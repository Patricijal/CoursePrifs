package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    private Restaurant restaurant;
//    @OneToOne
//    private FoodOrder order;
//    private int rating;
//    private boolean isVerified;
//    @Transient//konv
//    private List<String> photos;
//
//    public Review() {
//        super();
////        this.messageType = MessageType.REVIEW;
//        this.isVerified = false;
//    }

//    public Review(Restaurant restaurant, FoodOrder order, int rating, boolean isVerified, List<String> photos) {
//        super(customer, comment, MessageType.REVIEW);
//        this.restaurant = restaurant;
//        this.order = order;
//        this.rating = rating;
//        this.isVerified = true;
//    }
}
