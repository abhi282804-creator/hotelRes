package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bean.LoginBean;
import com.util.DBConnection;

public class LoginDao {

    // SQL Queries
    private static final String INSERT_USERS_SQL =
            "INSERT INTO customers (first_name, last_name, email, phone, password) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_USER_SQL =
            "SELECT * FROM customers WHERE email = ? AND password = ?";

    // Get the database connection via centralized DBConnection
    protected Connection getConnection() {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    // Method to validate the user during login
    public boolean validate(LoginBean loginBean) {

        boolean status = false;

        try (
            Connection connection = getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(SELECT_USER_SQL)
        ) {

            preparedStatement.setString(1, loginBean.getEmail());
            preparedStatement.setString(2, loginBean.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            status = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public static class CustomerRecord {
        private final int customerId;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;

        public CustomerRecord(int customerId, String firstName, String lastName, String email, String phone) {
            this.customerId = customerId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }
    }

    public CustomerRecord getCustomerByEmail(String email) {
        String sql = "SELECT customer_id, first_name, last_name, email, phone FROM customers WHERE email = ?";
        try (
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CustomerRecord(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to register a new user
    public void register(
            String firstName,
            String lastName,
            String email,
            String phone,
            String password
    ) throws SQLException {

        try (
            Connection connection = getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(INSERT_USERS_SQL)
        ) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, password);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}