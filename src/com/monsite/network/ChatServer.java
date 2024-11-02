package com.monsite.network;

import com.monsite.repository.MessageRepository;
import com.monsite.model.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private ServerSocket serverSocket;
    private static ConcurrentHashMap<String, ClientHandler> clientHandlers = new ConcurrentHashMap<>();
    private MessageRepository messageRepository;

    public ChatServer(int port, Connection connection) {
        this.messageRepository = new MessageRepository(connection);
        try {
            serverSocket = new ServerSocket(port);  // Utilisation du port 12345 pour toutes les connexions
            System.out.println("Serveur de chat démarré sur le port " + port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, ClientHandler excludeUser) {
        for (ClientHandler clientHandler : clientHandlers.values()) {
            try {
                if (clientHandler != excludeUser) {
                    clientHandler.writer.write(message);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
            } catch (IOException e) {
                clientHandler.closeEverything();
                clientHandlers.remove(clientHandler.username);
                e.printStackTrace();
            }
        }
    }

    public static void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler.username);
        broadcastMessage(clientHandler.username + " a quitté la discussion.", clientHandler);
    }

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/swe_appdesign_levelx", "root", "Linkzeld2.");
            new ChatServer(12345, connection);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.username = reader.readLine();
                clientHandlers.put(username, this);
                broadcastMessage(username + " a rejoint la discussion.", this);

                // Envoyer les messages en attente de la base de données
                List<Message> pendingMessages = messageRepository.findPendingMessagesByReceiverId(getUserIdByUsername(username));
                for (Message message : pendingMessages) {
                    writer.write("[Message en attente] " + message.getContent());
                    writer.newLine();
                    writer.flush();
                }
                messageRepository.markMessagesAsDelivered(getUserIdByUsername(username));  // Marquer les messages comme livrés après envoi
            } catch (IOException | SQLException e) {
                closeEverything();
            }
        }

        @Override
        public void run() {
            String messageFromClient;
            try {
                while ((messageFromClient = reader.readLine()) != null) {
                    System.out.println(messageFromClient);
                    broadcastMessage(messageFromClient, this);
                }
            } catch (IOException e) {
                closeEverything();
            }
        }

        public void closeEverything() {
            try {
                removeClientHandler(this);
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

        // Helper method to get user ID by username
        private Long getUserIdByUsername(String username) throws SQLException {
            // Query to get user ID by username
            String query = "SELECT idusers FROM users WHERE username = ?";
            try (PreparedStatement pstmt = messageRepository.getConnection().prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("idusers");
                    }
                }
            }
            return null;
        }
    }
}