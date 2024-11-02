package com.monsite.controller;

import com.monsite.model.User;
import com.monsite.repository.UserRepository;
import com.monsite.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class UserController {
    private UserService userService;
    private Connection connection;

    public UserController() throws SQLException {
        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/resources/application.properties")) {
                props.load(fis);
            }

            String url = props.getProperty("spring.datasource.url");
            String username = props.getProperty("spring.datasource.username");
            String password = props.getProperty("spring.datasource.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            createTables(); // Créer les tables si elles n'existent pas
            UserRepository userRepository = new UserRepository(connection);
            userService = new UserService(userRepository);
        } catch (ClassNotFoundException | IOException e) {
            throw new SQLException("Erreur lors du chargement du driver JDBC ou des propriétés : " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "idusers INT NOT NULL AUTO_INCREMENT,"
                    + "lastname VARCHAR(45) NOT NULL,"
                    + "firstname VARCHAR(45) NOT NULL,"
                    + "email VARCHAR(100) NOT NULL,"
                    + "username VARCHAR(45) NOT NULL,"
                    + "password VARCHAR(45) NOT NULL,"
                    + "status VARCHAR(10) DEFAULT 'offline',"
                    + "PRIMARY KEY (idusers)"
                    + ")";
            stmt.executeUpdate(createUserTable);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Veuillez fournir une commande valide : login ou create");
            return;
        }

        try {
            UserController userController = new UserController();
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

    public void login(String username, String password) {
        try {
            User user = userService.authenticateUser(username, password);
            if (user != null) {
                System.out.println("Connexion réussie !");
                System.exit(0);
            } else {
                System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
            System.exit(1);
        }
    }

    public void createProfile(String lastname, String firstname, String email, String username, String password) {
        try {
            User newUser = new User(lastname, firstname, email, username, password, "offline");
            userService.saveUser(newUser);
            System.out.println("Profil créé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du profil : " + e.getMessage());
        }
    }
}