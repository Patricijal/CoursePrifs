package com.example.courseprifs.consoleCourseWork;

import com.example.courseprifs.model.User;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Utils {
    public static void writeUserToFile(User user) {
        ObjectOutputStream out = null;
        try(var file = new FileOutputStream("o.txt")) {
            out = new ObjectOutputStream(new BufferedOutputStream(file));
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
//       ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(newFileInputStream("o.txt")));
//        Object o2 = in.readObject();
//        in.close();
    }
}
