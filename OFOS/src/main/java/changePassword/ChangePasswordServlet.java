package changePassword;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/ChangePassword")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ChangePasswordServlet() {
        super();
    }

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the user's email from the session
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        // Get the parameters from the form
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String repeatNewPassword = request.getParameter("repeat_new_password");

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

            // Prepare SQL query to retrieve the user's current password
            String sql = "SELECT password FROM Users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            // Execute the query to get the current password
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (currentPassword.equals(storedPassword) && newPassword.equals(repeatNewPassword)) {
                    // Password change logic
                    String updateQuery = "UPDATE Users SET password = ? WHERE email = ?";
                    pstmt = conn.prepareStatement(updateQuery);
                    pstmt.setString(1, newPassword);
                    pstmt.setString(2, email);

                    // Execute the update query to change the password
                    pstmt.executeUpdate();

                    // Display an alert message using JavaScript
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('Password changed successfully!'); window.location='UserProfile.jsp';</script>");
                } else {
                    // Passwords don't match, show an error message
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('Password change failed. Please make sure the new passwords match and the current password is correct.'); window.location='UpdateProfile.html#account-change-password';</script>");
                }
            } else {
                // User not found, show an error message
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
