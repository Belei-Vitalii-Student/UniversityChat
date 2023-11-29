package com.university.chat.universitychat.service;

import com.university.chat.universitychat.Logger;

import java.sql.*;

public class NewsService {
    private Connection connection;
    private Logger logger;

    public NewsService(Connection connection, Logger logger) throws Exception {
        this.connection = connection;
        this.logger = logger;

        Statement statement = connection.createStatement();
//        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM pg_tables WHERE tablename = 'news');");
        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE tbl_name = 'news');");

        boolean tableExists = rs.next() && rs.getBoolean(1);

        if(!tableExists) {
            createNewsTable();
        }
        statement.close();

    }

    public ResultSet selectAllNews() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT news.id, username, description FROM news JOIN users ON users.id=news.author";
        ResultSet data = statement.executeQuery(sql);
        return data;
    }

    private void createNewsTable() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE news (ID SERIAL PRIMARY KEY, author INT, description VARCHAR(255), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (author) REFERENCES users(id))";
        statement.executeUpdate(sql);
        this.logger.log("CREATE", "News table created");
        statement.close();
    }

    public boolean newNews(Integer author, String description) throws Exception {
        try {
            String sql = "INSERT INTO news (author, description) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, author);
                preparedStatement.setString(2, description);

                preparedStatement.executeUpdate();
            }

            logger.log("ADD", "Added new news by user with id: [" + author + "]");
            return true;
        } catch (SQLException e) {
            // Handle SQL exceptions appropriately (e.g., log or throw a custom exception)
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNews(Integer id) throws SQLException {
        String sql = "DELETE FROM news WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            this.logger.log("DELETE", "Deleted news with id: [" + id + "]");
        }

        return true;
    }
}

