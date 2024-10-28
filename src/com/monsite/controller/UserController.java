package com.monsite.controller;

import com.monsite.model.User;
import com.monsite.repository.UserRepository;
import com.monsite.service.UserService;
import com.monsite.service.MessageService;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class UserController {
    private UserService userService;
    private MessageService messageService;
    private Connection connection;
    private User currentUser;

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

            createTables();
        } catch (ClassNotFoundException | IOException e) {
            throw new SQLException("Erreur lors du chargement du driver JDBC ou des propriétés : " + e.getMessage());
        }
        UserRepository userRepository = new UserRepository(connection);
        userService = new UserService(userRepository);
        messageService = new MessageService(connection);
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "idusers INT NOT NULL AUTO_INCREMENT,"
                    + "lastname VARCHAR(45) NOT NULL,"
                    + "firstname VARCHAR(45) NOT NULL,"
                    + "age VARCHAR(45) NOT NULL,"
                    + "username VARCHAR(45) NOT NULL,"
                    + "password VARCHAR(45) NOT NULL,"
                    + "status VARCHAR(10) DEFAULT 'offline',"
                    + "PRIMARY KEY (idusers)"
                    + ")";
            stmt.executeUpdate(createUserTable);

            String createMessageTable = "CREATE TABLE IF NOT EXISTS messages ("
                    + "id INT NOT NULL AUTO_INCREMENT,"
                    + "sender_id INT NOT NULL,"
                    + "receiver_id INT NOT NULL,"
                    + "content VARCHAR(255) NOT NULL,"
                    + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "is_pending BOOLEAN DEFAULT TRUE,"
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (sender_id) REFERENCES users(idusers),"
                    + "FOREIGN KEY (receiver_id) REFERENCES users(idusers)"
                    + ")";
            stmt.executeUpdate(createMessageTable);
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choisissez une option : 1. Se connecter 2. Créer un profil 3. Quitter");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            try {
                switch (choice) {
                    case 1:
                        System.out.println("Entrez votre nom d'utilisateur :");
                        String username = scanner.nextLine();
                        System.out.println("Entrez votre mot de passe :");
                        String password = scanner.nextLine();
                        currentUser = userService.authenticateUser(username, password);
                        if (currentUser != null) {
                            currentUser.setStatus("online");
                            userService.updateUserStatus(currentUser);
                            System.out.println("Connexion réussie ! Bienvenue " + currentUser.getFirstname());
                            userMenu();
                        } else {
                            System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
                        }
                        break;
                    case 2:
                        System.out.println("Entrez le nom de famille :");
                        String lastname = scanner.nextLine();
                        System.out.println("Entrez le prénom :");
                        String firstname = scanner.nextLine();
                        System.out.println("Entrez l'âge :");
                        String age = scanner.nextLine();
                        System.out.println("Entrez le nom d'utilisateur :");
                        username = scanner.nextLine();
                        System.out.println("Entrez le mot de passe :");
                        password = scanner.nextLine();
                        User newUser = new User(lastname, firstname, age, "offline", username, password);
                        userService.saveUser(newUser);
                        System.out.println("Profil créé avec succès ! Veuillez vous connecter.");
                        break;
                    case 3:
                        System.out.println("Fermeture de l'application.");
                        if (currentUser != null) {
                            currentUser.setStatus("offline");
                            userService.updateUserStatus(currentUser);
                        }
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

    private void userMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choisissez une option : 1. Voir tous les utilisateurs 2. Envoyer un message 3. Déconnexion");
            int choice = scanner.nextInt();
            scanner.nextLine();
            try {
                switch (choice) {
                    case 1:
                        List<User> users = userService.getAllUsers();
                        for (User user : users) {
                            System.out.println(user.getFirstname() + " " + user.getLastname() + " - Age: " + user.getAge() + " - Status: " + user.getStatus());
                        }
                        break;
                    case 2:
                        System.out.println("Entrez le nom d'utilisateur du destinataire :");
                        String receiverUsername = scanner.nextLine();
                        User receiver = userService.findByUsername(receiverUsername);
                        if (receiver != null) {
                            System.out.println("Entrez votre message :");
                            String content = scanner.nextLine();
                            messageService.sendMessage(currentUser.getId(), receiver.getId(), content);
                            System.out.println("Message envoyé avec succès !");
                            if (receiver.getStatus().equals("offline")) {
                                System.out.println("Le destinataire est hors ligne. Le message sera livré lorsqu'il se connectera.");
                            }
                        } else {
                            System.out.println("Utilisateur introuvable.");
                        }
                        break;
                    case 3:
                        System.out.println("Déconnexion réussie.");
                        currentUser.setStatus("offline");
                        userService.updateUserStatus(currentUser);
                        currentUser = null;
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