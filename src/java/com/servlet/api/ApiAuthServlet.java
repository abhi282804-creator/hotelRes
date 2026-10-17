package com.servlet.api;

import com.bean.LoginBean;
import com.dao.LoginDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/auth")
public class ApiAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginDao loginDao;

    @Override
    public void init() {
        loginDao = new LoginDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String userEmail = (String) session.getAttribute("user");
            Integer customerId = (Integer) session.getAttribute("customerId");
            String firstName = (String) session.getAttribute("customerFirstName");
            String lastName = (String) session.getAttribute("customerLastName");
            String phone = (String) session.getAttribute("customerPhone");

            if (customerId == null && userEmail != null) {
                LoginDao.CustomerRecord cust = loginDao.getCustomerByEmail(userEmail);
                if (cust != null) {
                    customerId = cust.getCustomerId();
                    firstName = cust.getFirstName();
                    lastName = cust.getLastName();
                    phone = cust.getPhone();
                    session.setAttribute("customerId", customerId);
                    session.setAttribute("customerLegacyId", String.format("C%05d", customerId));
                    session.setAttribute("customerFirstName", firstName);
                    session.setAttribute("customerLastName", lastName);
                    session.setAttribute("customerPhone", phone);
                }
            }

            out.write("{\"authenticated\":true,\"user\":{" +
                "\"email\":\"" + escapeJson(userEmail) + "\"," +
                "\"customerId\":" + (customerId != null ? customerId : "null") + "," +
                "\"firstName\":\"" + escapeJson(firstName != null ? firstName : "") + "\"," +
                "\"lastName\":\"" + escapeJson(lastName != null ? lastName : "") + "\"," +
                "\"phone\":\"" + escapeJson(phone != null ? phone : "") + "\"" +
                "}}");
        } else {
            out.write("{\"authenticated\":false}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            // Check if JSON body parameter action is present or default to login
            action = "login";
        }

        if ("signup".equalsIgnoreCase(action)) {
            handleSignup(request, response, out);
        } else if ("logout".equalsIgnoreCase(action)) {
            handleLogout(request, response, out);
        } else {
            handleLogin(request, response, out);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\":false,\"message\":\"Email and password are required\"}");
            return;
        }

        LoginBean loginBean = new LoginBean();
        loginBean.setEmail(email.trim());
        loginBean.setPassword(password.trim());

        if (loginDao.validate(loginBean)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", email.trim());

            LoginDao.CustomerRecord cust = loginDao.getCustomerByEmail(email.trim());
            Integer customerId = null;
            String firstName = "";
            String lastName = "";
            String phone = "";
            if (cust != null) {
                customerId = cust.getCustomerId();
                firstName = cust.getFirstName();
                lastName = cust.getLastName();
                phone = cust.getPhone();
                session.setAttribute("customerId", customerId);
                session.setAttribute("customerLegacyId", String.format("C%05d", customerId));
                session.setAttribute("customerFirstName", firstName);
                session.setAttribute("customerLastName", lastName);
                session.setAttribute("customerPhone", phone);
            }

            out.write("{\"success\":true,\"message\":\"Login successful\",\"user\":{" +
                "\"email\":\"" + escapeJson(email.trim()) + "\"," +
                "\"customerId\":" + (customerId != null ? customerId : "null") + "," +
                "\"firstName\":\"" + escapeJson(firstName) + "\"," +
                "\"lastName\":\"" + escapeJson(lastName) + "\"," +
                "\"phone\":\"" + escapeJson(phone) + "\"" +
                "}}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\":false,\"message\":\"Invalid email or password\"}");
        }
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()
                || firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"success\":false,\"message\":\"All required fields must be filled\"}");
            return;
        }

        try {
            loginDao.register(firstName.trim(), lastName.trim(), email.trim(), phone != null ? phone.trim() : "", password.trim());
            out.write("{\"success\":true,\"message\":\"Account created successfully. Please login.\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\":false,\"message\":\"Unable to create account. Email may already exist or database error.\"}");
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        out.write("{\"success\":true,\"message\":\"Logged out successfully\"}");
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}
