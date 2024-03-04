package DeleteServlet;

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

@WebServlet("/DeleteAccount")
public class DeleteAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteAccountServlet() {
        super();
    }

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        // Database connection parameters
        String dbUrl = "jdbc:mysql://localhost:3306/ofos";
        String dbUsername = "root";
        String dbPassword = "plb123";

        // JDBC connection variables
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the database driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Start a transaction
            conn.setAutoCommit(false);

            // Prepare SQL query to delete user information from both tables
            String deleteQueryCustomer = "DELETE FROM Customer WHERE UID IN (SELECT UID FROM Users WHERE email = ?)";
            pstmt = conn.prepareStatement(deleteQueryCustomer);
            pstmt.setString(1, email);

            // Execute the delete query for the Customer table
            int rowsAffectedCustomer = pstmt.executeUpdate();

            // Commit the transaction for the Customer table
            conn.commit();

            if (rowsAffectedCustomer > 0) {
                // Prepare SQL query to delete user information from the Users table
                String deleteQueryUsers = "DELETE FROM Users WHERE email = ?";
                pstmt = conn.prepareStatement(deleteQueryUsers);
                pstmt.setString(1, email);

                // Execute the delete query for the Users table
                int rowsAffectedUsers = pstmt.executeUpdate();

                // Commit the transaction for the Users table
                conn.commit();

                if (rowsAffectedUsers > 0) {
                    // Deletion successful
                    // You may also want to invalidate the session to log out the user.
                    session.invalidate();

                    // Redirect with an alert message
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Your account has been deleted!');");
                    out.println("window.location.href='registration.jsp';");
                    out.println("</script>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            // Roll back the transaction if an error occurs
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Close resources and set auto-commit back to true
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
