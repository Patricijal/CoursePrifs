package com.example.courseprifs.fxControllers;

import com.example.courseprifs.hibernateControl.GenericHibernate;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatForm {
    public ListView messageList;
    public TextArea messageBody;
    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    public void sendMessage() {
    }
}
