package com.example.courseprifs.consoleCourseWork;

import com.example.courseprifs.model.FoodOrder;
import com.example.courseprifs.model.User;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// @NoArgsConstructor //lombok annotation to generate a no-argument constructor, null values
public class Wolt implements Serializable {
    private List<User> allSystemUsers;
    private List<FoodOrder> allOrders;

    public Wolt() { //Overloading issue with @NoArgsConstructor
        this.allOrders = new ArrayList<>();
        this.allSystemUsers = new ArrayList<>();
    }

    public List<User> getAllSystemUsers() {
        return allSystemUsers;
    }

    public void setAllSystemUsers(List<User> allSystemUsers) {
        this.allSystemUsers = allSystemUsers;
    }

}
