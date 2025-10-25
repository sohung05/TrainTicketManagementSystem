/*
 * @ (#) BaseDAO.java          1.0        10/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package dao;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 10/25/2025
 * @version:    1.0
 */
import connectDB.connectDB;
import java.sql.Connection;

public abstract class BaseDAO {
    protected Connection getConnection() {
        // đúng với ConnectDB bạn đã viết (có getCon())
        return connectDB.getInstance().getCon();
    }

    protected String n(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}
