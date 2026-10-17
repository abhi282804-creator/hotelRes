package com.servlet;

import com.bean.LoginBean;
import com.dao.LoginDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginDao loginDao;

    public void init() {
        loginDao = new LoginDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("signup".equals(action)) {
            handleSignup(request, response);
        }
    }

    // Handle the login logic
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email);
        loginBean.setPassword(password);

        if (loginDao.validate(loginBean)) {
            // Redirect to a dashboard or welcome page upon successful login
            response.sendRedirect("dashboard.jsp");
        } else {
            // If validation fails, redirect back to login page with error message
            response.sendRedirect("Login.jsp?error=Invalid email or password");
        }
    }

    // Handle the sign-up logic
    private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        try {
            // Register the user into the database
            loginDao.register(firstName, lastName, email, phone, password);
            // Redirect to login page with success message after successful registration
            response.sendRedirect("Login.jsp?success=Account created successfully. Please login.");
        } catch (SQLException e) {
            e.printStackTrace();
            // If any error occurs during registration, redirect back to signup page with error message
            response.sendRedirect("Login.jsp?error=Unable to create account. Please try again.");
        }
    }
}
