package LoginServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Database connection parameters
        String dbUrl = "jdbc:mysql://localhost:3306/ofos";
        String dbUsername = "root";
        String dbPassword = "plb123";

        // JDBC connection variables
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Load the database driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Prepare SQL query to retrieve user information
            String sql = "SELECT UID, type FROM Users WHERE email = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            // Execute the query
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // User found in the database
                String userType = rs.getString("type");

                // Create a session and store user information
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                session.setAttribute("userType", userType);

                if ("customer".equals(userType)) {
                    // Retrieve customer details from the database
                    String customerInfoQuery = "SELECT u.email, u.password, c.name, c.phone_number FROM Users u " +
                                              "JOIN Customer c ON u.UID = c.UID " +
                                              "WHERE u.email = ?";
                    pstmt = conn.prepareStatement(customerInfoQuery);
                    pstmt.setString(1, email);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String customerName = rs.getString("name");
                        String customerEmail = rs.getString("email");
                        String customerPhoneNumber = rs.getString("phone_number");
                        String customerPassword = rs.getString("password");

                        // Store customer details in the session
                        session.setAttribute("name", customerName);
                        session.setAttribute("email", customerEmail);
                        session.setAttribute("phone_number", customerPhoneNumber);
                        session.setAttribute("password", customerPassword);

                        response.sendRedirect("index.jsp");
                    } else {
                        // Handle the case where customer details are not found
                    }
                } else if ("admin".equals(userType)) {
                    response.sendRedirect("Admin.jsp");
                } else if ("staff".equals(userType)) {
                    response.sendRedirect("Staff.jsp");
                } else if ("deliverer".equals(userType)) {
                    response.sendRedirect("delivery.jsp");
                } else {
                    // Handle other types if needed
                }
            } else {
                // User not found
                response.sendRedirect("Login.jsp"); // Redirect back to the login page
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
