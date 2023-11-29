package com.university.chat.universitychat;

import com.university.chat.universitychat.service.MeetingService;
import com.university.chat.universitychat.service.NewsService;
import com.university.chat.universitychat.service.ScheduleService;
import com.university.chat.universitychat.service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PostgresqlDB {
    private Connection connection;
    private Logger logger;
    private Integer actualUserId;
    private UserService userService;
    private NewsService newsService;
    private ScheduleService scheduleService;
    private MeetingService meetingService;

    PostgresqlDB() throws Exception {
//        DriverManager.registerDriver(new org.postgresql.Driver());
//
//        String url = "jdbc:postgresql://localhost:5432/university_hub";
//        String username = "postgres";
//        String password = "admin";


        DriverManager.deregisterDriver(new org.postgresql.Driver());
        DriverManager.registerDriver(new org.sqlite.JDBC());
        String url = "jdbc:sqlite:university_hub.db"; // Change 'university_hub' to the desired SQLite database filename

        String username = "";
        String password = "";

        this.connection = DriverManager.getConnection(url, username, password);
        this.logger = new Logger(this.connection);
        this.userService = new UserService(connection, logger);
        this.newsService = new NewsService(connection, logger);
        this.scheduleService = new ScheduleService(connection, logger);
        this.meetingService = new MeetingService(connection, logger);

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT EXISTS (SELECT 1 FROM sqlite_master WHERE tbl_name = 'roles');");

        boolean tableExists = rs.next() && rs.getBoolean(1);

        if(!tableExists) {
            createRolesTable();
        }
        statement.close();
    }

    private void createRolesTable() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE roles (ID SERIAL PRIMARY KEY, name VARCHAR(100))";
        statement.executeUpdate(sql);
        sql = "INSERT INTO roles (name) VALUES ('Student'); INSERT INTO roles (name) VALUES ('Teacher'); INSERT INTO roles (name) VALUES ('Admin')";
        statement.executeUpdate(sql);
        this.logger.log("CREATE", "Roles table created");
        statement.close();
    }

    public Integer validateData(String username, String password) throws Exception {
        ResultSet user = userService.findUserByUsername(username);
        if(!user.next()) {
            throw new Exception("User with this data doesn`t exists");
        }

        System.out.println(username + " | " + password);

        Integer id = user.getInt("ID");
        String userPassword = user.getString("PASSWORD");
        Integer role = user.getInt("ROLE");
        if(!password.equals(userPassword))
            throw new Exception("User with this data doesn`t exists");

        this.actualUserId = id;

        user.close();

        return role;
    }

    public Integer getActualUserId() {
        return this.actualUserId;
    }

    public ResultSet selectAllNews() throws SQLException {
        return newsService.selectAllNews();
    }

    public ResultSet findUserById(Integer id) throws SQLException {
        return userService.findUserById(id);
    }

    public List<Log> getLogs() throws SQLException {
        List<Log> logs = new ArrayList();

        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM logs";
        ResultSet set = statement.executeQuery(sql);
        while (set.next()) {
            logs.add(new Log(set.getInt("id"), set.getString("action"), set.getTimestamp("date"), set.getString("description")));
        }
        statement.close();

        return logs;
    }
}
