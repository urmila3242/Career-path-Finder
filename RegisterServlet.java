package com.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/reg")
public class RegisterServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String email = req.getParameter("email");
        String dob = req.getParameter("dob"); // this is in yyyy-MM-dd format
        String gender = req.getParameter("gender");
        String password = req.getParameter("password");

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load the JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish connection to the database
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");

            // Convert string DOB to java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(dob);
            Date sqlDate = new Date(parsedDate.getTime());

            // SQL insert query
            String sql = "INSERT INTO STUDENTS (name, address, email, dob, gender, password) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            // Create PreparedStatement
            ps = con.prepareStatement(sql);

            // Set parameters
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, email);
            ps.setDate(4, sqlDate);  // Set the converted date
            ps.setString(5, gender);
            ps.setString(6, password);

            // Execute query
            int result = ps.executeUpdate();

            if (result > 0) {
                out.println("<h3>Registration Successful!</h3>");
                out.println("<a href='login.html'>Click here to login</a>");
            } else {
                out.println("<h3>Failed to Register. Please Try Again!</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
