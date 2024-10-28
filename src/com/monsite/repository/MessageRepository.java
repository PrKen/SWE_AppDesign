package com.monsite.repository;

import com.monsite.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {
    private Connection connection;

    public MessageRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Message message) throws SQLException {
        String query = "INSERT INTO messages (sender_id, receiver_id, content, is_pending) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, message.getSenderId());
            pstmt.setLong(2, message.getReceiverId());
            pstmt.setString(3, message.getContent());
            pstmt.setBoolean(4, message.isPending());
            pstmt.executeUpdate();
        }
    }

    public List<Message> findPendingMessagesByReceiverId(Long receiverId) throws SQLException {
        String query = "SELECT * FROM messages WHERE receiver_id = ? AND is_pending = true";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, receiverId);
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

    public void markMessagesAsDelivered(Long receiverId) throws SQLException {
        String query = "UPDATE messages SET is_pending = false WHERE receiver_id = ? AND is_pending = true";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, receiverId);
            pstmt.executeUpdate();
        }
    }
}