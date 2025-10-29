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
    @ManyToOne
    private BasicUser owner;
    @ManyToOne
    private BasicUser feedBackOwner;
    @ManyToOne
    private Restaurant restaurant;
    @OneToOne
    private FoodOrder order;
    @ManyToOne
    private Chat chat;

    private int rating;
    private String text;

    public Review(int rating, String text) {
        this.rating = rating;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Rating: " + rating + " | Text: " + text;
    }
}
