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
    private int rating;
    private String text;
    @ManyToOne
    private BasicUser commentOwner;
    @ManyToOne
    private BasicUser feedbackUser;
    @ManyToOne
    private Restaurant restaurant;
    @OneToOne
    private FoodOrder order;
    @ManyToOne
    private Chat chat;

//    public Review(int rating, String text) {
//        this.rating = rating;
//        this.text = text;
//    }

    public Review(String text, BasicUser commentOwner, Chat chat) {
        this.text = text;
        this.commentOwner = commentOwner;
        this.chat = chat;
    }

    public Review(int rating, String text, BasicUser commentOwner, Restaurant restaurant) {
        this.rating = rating;
        this.text = text;
        this.commentOwner = commentOwner;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Rating: " + rating + " | Text: " + text;
    }
}
