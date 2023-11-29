package com.university.chat.universitychat.service;

import com.university.chat.universitychat.Logger;

import java.sql.*;

public class MeetingService {
    private Connection connection;
    private Logger logger;

    public MeetingService(Connection connection, Logger logger) throws Exception {
        this.connection = connection;
        this.logger = logger;

        Statement statement = connection.createStatement();
//        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'meeting');");
        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE tbl_name = 'meeting');");

        boolean tableExists = rs.next() && rs.getBoolean(1);

        if(!tableExists) {
            createMeetingTable();
        }
        statement.close();
    }

    public ResultSet selectAllMeetings() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM meeting";
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

    private void createMeetingTable() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE meeting (\n" +
                "    id SERIAL PRIMARY KEY," +
                "    author INT NOT NULL," +
                "    date TIMESTAMP NOT NULL," +
                "    description VARCHAR(255) NOT NULL," +
                "    link VARCHAR(100) NOT NULL," +
                "    FOREIGN KEY (author) REFERENCES users(id)" +
                ");";
        statement.executeUpdate(sql);
        this.logger.log("CREATE", "Schedule table created");
        statement.close();
    }

    public boolean newMeeting(Integer author, Timestamp date, String description, String link) throws Exception {
        try {
            String sql = "INSERT INTO meeting (author, date, description, link) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, author);
                preparedStatement.setTimestamp(2, date);
                preparedStatement.setString(3, description);
                preparedStatement.setString(4, link);

                preparedStatement.executeUpdate();
            }

            logger.log("ADD", "Added new meeting from author:" + author);
            return true;
        } catch (SQLException e) {
            // Handle SQL exceptions appropriately (e.g., log or throw a custom exception)
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMeeting(Integer id) throws SQLException {
        String sql = "DELETE FROM meeting WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            this.logger.log("DELETE", "Deleted meeting with id: [" + id + "]");
        }

        return true;
    }
}
