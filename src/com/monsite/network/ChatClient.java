package com.monsite.network;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;
    private String targetUsername;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ChatClient(String username, String targetUsername, int port) {
        this.username = username;
        this.targetUsername = targetUsername;
        try {
            socket = new Socket("localhost", port);  // Connexion à localhost sur le port fixe 12345
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Envoi du nom d'utilisateur au serveur
            writer.write(username);
            writer.newLine();
            writer.flush();

            // Thread pour lire les messages du serveur
            new Thread(() -> {
                try {
                    String messageFromServer;
                    while ((messageFromServer = reader.readLine()) != null) {
                        System.out.println(messageFromServer);
                    }
                } catch (IOException e) {
                    closeEverything(socket, reader, writer);
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            // Envoyer des messages au serveur
            while (socket.isConnected()) {
                System.out.print("Vous: ");
                String messageToSend = scanner.nextLine();
                if (messageToSend.equalsIgnoreCase("/leave")) {
                    System.out.println("Vous avez quitté la discussion.");
                    closeEverything(socket, reader, writer);
                    break;
                } else if (messageToSend.equalsIgnoreCase("/help")) {
                    System.out.println("Commandes disponibles :");
                    System.out.println("/leave - Quitter la discussion");
                    System.out.println("/list - Voir les utilisateurs connectés");
                } else {
                    String timestamp = LocalDateTime.now().format(formatter);
                    String formattedMessage = "[" + timestamp + "] " + username + " à " + targetUsername + ": " + messageToSend;
                    writer.write(formattedMessage);
                    writer.newLine();
                    writer.flush();
                }
            }
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java ChatClient <username> <targetUsername> <port>");
            return;
        }
        String username = args[0];
        String targetUsername = args[1];
        int port = Integer.parseInt(args[2]);
        new ChatClient(username, targetUsername, port);
    }
}