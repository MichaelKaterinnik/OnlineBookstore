package com.onlinebookstore.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
        private DatabaseConnection() {
        }

        public static Connection getConnection() throws SQLException {
            return DBConnectionPooling.getConnection();
        }
}
