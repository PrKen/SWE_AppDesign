package com.monsite.controller;

import com.monsite.model.User;
import com.monsite.repository.UserRepository;
import com.monsite.service.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class UserController {
    private UserService userService;
    private Connection connection;

    public UserController() throws SQLException {
        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/ressources/application.properties")) {
                props.load(fis);
            }

            String url = props.getProperty("spring.datasource.url");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | IOException e) {
            throw new SQLException("Erreur lors du chargement du driver JDBC ou des propriétés : " + e.getMessage());
        }
        UserRepository userRepository = new UserRepository(connection);
        userService = new UserService(userRepository);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choisissez une option : 1. Voir tous les utilisateurs 2. Ajouter un utilisateur 3. Quitter");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            try {
                switch (choice) {
                    case 1:
                        List<User> users = userService.getAllUsers();
                        for (User user : users) {
                            System.out.println(user.getFirstname() + " " + user.getLastname() + " - Age: " + user.getAge() + " - Status: " + user.getStatus());
                        }
                        break;
                    case 2:
                        System.out.println("Entrez le nom de famille :");
                        String lastname = scanner.nextLine();
                        System.out.println("Entrez le prénom :");
                        String firstname = scanner.nextLine();
                        System.out.println("Entrez l'âge :");
                        String age = scanner.nextLine();
                        System.out.println("Entrez le statut :");
                        String status = scanner.nextLine();
                        User user = new User(lastname, firstname, age, status);
                        userService.saveUser(user);
                        break;
                    case 3:
                        System.out.println("Fermeture de l'application.");
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Option non reconnue.");
                }
            } catch (SQLException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }
}