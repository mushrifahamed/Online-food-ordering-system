package UpdateServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@WebServlet("/Update")
public class UpdateProfile extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProfile() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the user's updated information from the form
        String newName = request.getParameter("name");
        String newEmail = request.getParameter("email");
        String newPhoneNumber = request.getParameter("phone_number");

        // Database connection parameters
        String dbUrl = "jdbc:mysql://localhost:3306/ofos";
        String dbUsername = "root";
        String dbPassword = "plb123";

        // JDBC connection variables
        Connection conn = null;
        PreparedStatement pstmt = null;

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        try {
            // Load the database driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Prepare SQL query to update user information
            String updateQuery = "UPDATE Customer SET name = ?, email = ?, phone_number = ? WHERE UID IN (SELECT UID FROM Users WHERE email = ?)";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, newName);
            pstmt.setString(2, newEmail);
            pstmt.setString(3, newPhoneNumber);
            pstmt.setString(4, email);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Update successful, update session attributes
                session.setAttribute("name", newName);
                session.setAttribute("email", newEmail);
                session.setAttribute("phone_number", newPhoneNumber);
                
                // Redirect with an alert message
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Your profile has been updated!');");
                out.println("window.location.href='UserProfile.jsp';");
                out.println("</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
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

