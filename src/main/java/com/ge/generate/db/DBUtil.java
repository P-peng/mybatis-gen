package com.ge.generate.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 工具类，获取数据库的连接
 */
public class DBUtil {

    private static Connection connection = null;

    // 返回数据库连接
    public static Connection getConnection(String drive, String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(drive);
        // 获取数据库连接
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static Connection getConnection() throws Exception {
        // 获取数据库连接
        if (connection == null) {
            throw new RuntimeException("无连接");
        }
        return connection;
    }
}

