package com.university.chat.universitychat.service;

import com.university.chat.universitychat.Logger;

import java.sql.*;

public class ScheduleService {
    private Connection connection;
    private Logger logger;

    public ScheduleService(Connection connection, Logger logger) throws Exception {
        this.connection = connection;
        this.logger = logger;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'schedule');");

        boolean tableExists = rs.next() && rs.getBoolean(1);

        if(!tableExists) {
            createScheduleTable();
        }
        statement.close();
    }

    public ResultSet selectAllNews() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users";
        ResultSet data = statement.executeQuery(sql);
        while (data.next()) {
            Integer id = data.getInt("ID");
            Integer author = data.getInt("AUTHOR");
            Timestamp date = data.getTimestamp("DATE");
            String description = data.getString("DESCRIPTION");
            System.out.println(id + " | " + author + " | " + date);
        }
        statement.close();
        return data;
    }

    private void createScheduleTable() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE schedule (\n" +
                "    id SERIAL PRIMARY KEY," +
                "    day_of_week VARCHAR(10) NOT NULL," +
                "    position INT NOT NULL," +
                "    subject VARCHAR(100) NOT NULL," +
                "    room_number VARCHAR(10) NOT NULL," +
                "    group_name VARCHAR(255) NOT NULL," +
                "    teacher INT NOT NULL," +
                "    FOREIGN KEY (teacher) REFERENCES users(id)" +
                ");";
        statement.executeUpdate(sql);
        this.logger.log("CREATE", "Schedule table created");
        statement.close();
    }

    public boolean newNote(String dayOfWeek, Integer position, String subject, String roomName, String groupName, Integer teacher) throws Exception {
        try {
            String sql = "INSERT INTO schedule (dayOfWeek, position, subject, roomName, groupName, teacher) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, dayOfWeek);
                preparedStatement.setInt(2, position);
                preparedStatement.setString(3, subject);
                preparedStatement.setString(4, roomName);
                preparedStatement.setString(5, groupName);
                preparedStatement.setInt(6, teacher);

                preparedStatement.executeUpdate();
            }

            logger.log("ADD", "Added new note to schedule for teacher:" + teacher);
            return true;
        } catch (SQLException e) {
            // Handle SQL exceptions appropriately (e.g., log or throw a custom exception)
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNote(Integer id) throws SQLException {
        String sql = "DELETE FROM schedule WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            this.logger.log("DELETE", "Deleted note with id: [" + id + "]");
        }

        return true;
    }
}
