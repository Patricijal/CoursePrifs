package com.example.courseprifs.consoleCourseWork;

import com.example.courseprifs.model.Driver;
import com.example.courseprifs.model.User;
import com.example.courseprifs.model.VehicleType;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuControl {
    public static void generateUserMenu(Scanner scanner, Wolt wolt) {
        var cmd = 0;
        while (cmd != 6) {
            System.out.println("""
                    Choose and option:
                    1 - create
                    2 - view all users
                    3 - view specific user
                    4 - update
                    5 - delete
                    6 - return to main menu
                    """);
            cmd = scanner.nextInt();
            scanner.nextLine();

            switch (cmd) {
                case 1:
                    System.out.println("Enter User data (User class):username;password;name;surname;phoneNum;address; licence; bdate;vehicle");
                    var input = scanner.nextLine();
                    String[] info = input.split(";");
                    User user = new User(info[0], info[1], info[2], info[3], info[4]);
                    //Driver driver = new Driver(info[0], info[1], info[2], info[3], info[4], info[5], info[6], LocalDate.parse(info[7]), VehicleType.valueOf(info[8]));
                    wolt.getAllSystemUsers().add(user);
                    break;
                case 2:
                    for (User u : wolt.getAllSystemUsers()) {
                        System.out.println(u);
                    }
                    break;
                case 3:
                    System.out.println("Enter user login:");
                    input = scanner.nextLine();
                    for (User u : wolt.getAllSystemUsers()) {
                        if (u.getLogin().contains(input)) {
                            System.out.println(u);
                        }
                    }
                    //wolt.getAllSystemUsers().stream().filter(user1 -> user1.getLogin().contains(input)).findFirst().ifPresent(user1 -> {});
                    break;
                case 4:
                    System.out.println("Enter user login:");
                    input = scanner.nextLine();
                    for (User u : wolt.getAllSystemUsers()) {
                        if (u.getLogin().contains(input)) {
                            System.out.println("Enter data to update: name; surname");
                            String[] infoUpdate = scanner.nextLine().split(";");
                            u.setName(infoUpdate[0]);
                            u.setSurname(infoUpdate[1]);
                        }
                    }
                case 5:
                    System.out.println("Enter user login:");
                    input = scanner.nextLine();
                    for (User u : wolt.getAllSystemUsers()) {
                        if (u.getLogin().equals(input)) {
                            wolt.getAllSystemUsers().remove(u);
                            break;
                        }
                    }
                    break;
                default:
                    System.out.println();
            }
        }
    }
}
