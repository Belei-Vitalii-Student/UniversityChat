package com.university.chat.universitychat.service;

import com.university.chat.universitychat.Logger;

import java.sql.*;

public class UserService {
    private Connection connection;
    private Logger logger;

    public UserService(Connection connection, Logger logger) throws Exception {
        this.connection = connection;
        this.logger = logger;

        Statement statement = connection.createStatement();
//        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'users');");
        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE tbl_name = 'users');");

        boolean tableExists = rs.next() && rs.getBoolean(1);

        if(!tableExists) {
            createUserTable();
        }
        statement.close();
    }

    public ResultSet selectAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users";
        ResultSet data = statement.executeQuery(sql);
        return data;
    }

    public ResultSet selectAllTeachers() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users\n" +
                "JOIN roles ON roles.id=users.role\n" +
                "WHERE name = 'Teacher'";
        ResultSet data = statement.executeQuery(sql);
        statement.close();
        return data;
    }

    public ResultSet selectAllStudents() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users\n" +
                "JOIN roles ON roles.id=users.role\n" +
                "WHERE name = 'Student'";
        ResultSet data = statement.executeQuery(sql);
        statement.close();
        return data;
    }

    private void createUserTable() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE users (ID SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255), role INT, FOREIGN KEY (role) REFERENCES roles(id))";
        statement.executeUpdate(sql);
        this.logger.log("CREATE", "User table created");
        statement.close();
    }

    public boolean userAlreadyExist(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        ResultSet data = statement.executeQuery(sql);
        if(data.getFetchSize() != 0) {
            statement.close();
            return true;
        }

        statement.close();
        return false;
    }

    public ResultSet findUserById(Integer id) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users WHERE id = " + id + " LIMIT 1";
        System.out.println(sql);
        ResultSet data = statement.executeQuery(sql);
        return data;
    }

    public ResultSet findUserByUsername(String username) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM users WHERE username = '" + username + "' LIMIT 1";
        System.out.println(sql);
        ResultSet data = statement.executeQuery(sql);
        return data;
    }

    public boolean newUser(String username, String password, Integer role) throws Exception {
        try {
            if (userAlreadyExist(username)) {
                System.out.println("User with this username already exists");
                return false;
            }

            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setInt(3, role);

                preparedStatement.executeUpdate();
            }

            logger.log("ADD", "Added new user: [" + username + "]");
            return true;
        } catch (SQLException e) {
            // Handle SQL exceptions appropriately (e.g., log or throw a custom exception)
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(Integer id) throws SQLException {
        String sql = "DELETE FROM users WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            this.logger.log("DELETE", "Deleted user with id: [" + id + "]");
        }

        return true;
    }

    public boolean changeUsername(Integer id, String newUsername) throws SQLException {
        String sql = "UPDATE users SET username = ? WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            this.logger.log("UPDATE", "Updated username to [" + newUsername + "] for user: " + id);
        }

        return true;
    }

    public boolean changePassword(Integer id, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }

        return true;
    }
}
