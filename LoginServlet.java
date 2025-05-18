package com.login;

import java.io.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/log")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to Oracle DB
            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "system");

            // Query to check if user exists
            String query = "SELECT name FROM STUDENTS WHERE email = ? AND password = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                // Successful login
                String name = rs.getString("name");

                HttpSession session = req.getSession();
                session.setAttribute("username", name);

                res.sendRedirect("home.html");
            } else {
                // Invalid login
                out.println("<h3>Invalid email or password. Please try again.</h3>");
                out.println("<a href='login.html'>Back to Login</a>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
