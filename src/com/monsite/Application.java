package com.monsite;

import com.monsite.controller.UserController;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        try {
            UserController userController = new UserController();
            userController.start();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}