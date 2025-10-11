package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String title;
    @ManyToOne
    private BasicUser customer;

//    @ManyToOne
//    private Driver driver;
//    @OneToOne
//    private Chat chat;

//    private Restaurant restaurant;
//    private List<OrderItem> orderItems;
//    private OrderStatus status;
//    private double subtotal;
//    private double deliveryFee;
//    private double totalAmount;
//    private String deliveryAddress;
//    private String specialInstructions;
//    private LocalDateTime orderTime;
//    private LocalDateTime estimatedDeliveryTime;
//    private LocalDateTime actualDeliveryTime;
//    private PaymentMethod paymentMethod;
//    private String trackingNumber;

}
