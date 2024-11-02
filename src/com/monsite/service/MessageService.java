package com.monsite.service;

import com.monsite.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private Connection connection;

    public MessageService(Connection connection) {
        this.connection = connection;
    }

    public void sendMessage(Long senderId, Long receiverId, String content) throws SQLException {
        // Éviter d'envoyer des messages en double
        if (!messageAlreadyExists(senderId, receiverId, content)) {
            String query = "INSERT INTO messages (sender_id, receiver_id, content, is_pending) VALUES (?, ?, ?, true)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setLong(1, senderId);
                pstmt.setLong(2, receiverId);
                pstmt.setString(3, content);
                pstmt.executeUpdate();
            }
        }
    }

    public boolean messageAlreadyExists(Long senderId, Long receiverId, String content) throws SQLException {
        String query = "SELECT COUNT(*) FROM messages WHERE sender_id = ? AND receiver_id = ? AND content = ? AND is_pending = true";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, senderId);
            pstmt.setLong(2, receiverId);
            pstmt.setString(3, content);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Message> getPendingMessages(Long userId) throws SQLException {
        String query = "SELECT * FROM messages WHERE receiver_id = ? AND is_pending = true ORDER BY timestamp";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setReceiverId(rs.getLong("receiver_id"));
                    message.setContent(rs.getString("content"));
                    message.setTimestamp(rs.getTimestamp("timestamp"));
                    message.setPending(rs.getBoolean("is_pending"));
                    messages.add(message);
                }
            }
        }
        return messages;
    }

    public void markMessagesAsDelivered(Long userId) throws SQLException {
        String query = "UPDATE messages SET is_pending = false WHERE receiver_id = ? AND is_pending = true";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        }
    }
}