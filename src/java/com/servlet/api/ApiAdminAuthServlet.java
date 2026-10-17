package com.servlet.api;

import com.dao.AdminDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/admin/auth")
public class ApiAdminAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminDao adminDao;

    @Override
    public void init() {
        adminDao = new AdminDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("adminUser") != null) {
            String adminUser = (String) session.getAttribute("adminUser");
            String adminFullName = (String) session.getAttribute("adminFullName");
            out.write("{\"authenticated\":true,\"admin\":{\"username\":\"" + escapeJson(adminUser) + "\",\"fullName\":\"" + escapeJson(adminFullName != null ? adminFullName : "Administrator") + "\"}}");
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
        if (action == null || action.trim().isEmpty()) {
            action = "login";
        }

        if ("logout".equalsIgnoreCase(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute("adminUser");
                session.removeAttribute("adminFullName");
            }
            out.write("{\"success\":true,\"message\":\"Logged out successfully\"}");
            return;
        }

        if ("login".equalsIgnoreCase(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\":false,\"message\":\"Username and password are required\"}");
                return;
            }

            AdminDao.AdminUser user = adminDao.authenticate(username.trim(), password);
            if (user != null) {
                HttpSession session = request.getSession(true);
                request.changeSessionId();
                session.setAttribute("adminUser", user.getUsername());
                session.setAttribute("adminFullName", user.getFullName());
                out.write("{\"success\":true,\"message\":\"Admin authenticated successfully\",\"admin\":{\"username\":\"" + escapeJson(user.getUsername()) + "\",\"fullName\":\"" + escapeJson(user.getFullName()) + "\"}}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"success\":false,\"message\":\"Invalid username or password\"}");
            }
            return;
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.write("{\"success\":false,\"message\":\"Unknown action: " + escapeJson(action) + "\"}");
    }

    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
