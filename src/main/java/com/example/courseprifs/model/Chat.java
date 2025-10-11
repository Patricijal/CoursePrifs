package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Getter
//@Setter
//@AllArgsConstructor
//@Entity
public class Chat {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @ManyToOne
//    private BasicUser customer;
//    @ManyToOne
//    private Driver driver;
//    @OneToOne
//    private FoodOrder order;
//    @OneToMany
//    private List<ChatMessage> messages;
//    private LocalDateTime createdAt;
//    private LocalDateTime lastMessageAt;
//    private boolean isActive;
//
//    public Chat() {
//        this.messages = new ArrayList<>();
//        this.createdAt = LocalDateTime.now();
//        this.isActive = true;
//    }
//
//    public Chat(BasicUser customer, Driver driver, FoodOrder order) {
//        this();
//        this.customer = customer;
//        this.driver = driver;
//        this.order = order;
//    }
//
//    public void addMessage(ChatMessage message) {
//        this.messages.add(message);
//        this.lastMessageAt = message.getTimestamp();
//    }
}
