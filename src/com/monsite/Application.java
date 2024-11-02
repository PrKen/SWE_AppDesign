package com.monsite;

import com.monsite.controller.UserController;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {
        try {
            UserController userController = new UserController();
            if (args.length < 1) {
                System.out.println("Veuillez fournir une commande valide : login ou create");
                return;
            }

            String command = args[0];

            if (command.equals("login")) {
                if (args.length < 3) {
                    System.out.println("Usage: login <username> <password>");
                    return;
                }
                String username = args[1];
                String password = args[2];
                userController.login(username, password);
            } else if (command.equals("create")) {
                if (args.length < 6) {
                    System.out.println("Usage: create <lastname> <firstname> <email> <username> <password>");
                    return;
                }
                String lastname = args[1];
                String firstname = args[2];
                String email = args[3];
                String username = args[4];
                String password = args[5];
                userController.createProfile(lastname, firstname, email, username, password);
            } else {
                System.out.println("Commande non reconnue : " + command);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
}