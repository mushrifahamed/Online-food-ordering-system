package bookingServlet;

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
import java.io.PrintWriter;

@WebServlet(urlPatterns = { "/ReservationServlet" })
public class ReservationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReservationServlet() {
        super();
    }

    @SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email"); // Assuming the user is logged in

        // Get form data
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String reservationDate = request.getParameter("reservation_date");
        int persons = Integer.parseInt(request.getParameter("persons"));

        // Database connection parameters
        String dbUrl = "jdbc:mysql://localhost:3306/ofos";
        String dbUsername = "root";
        String dbPassword = "plb123";

        // JDBC connection variables
        Connection conn = null;
        PreparedStatement pstmt = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load the database driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Fetch the user's UID based on their email
            String selectUserQuery = "SELECT UID FROM Users WHERE email = ?";
            pstmt = conn.prepareStatement(selectUserQuery);
            pstmt.setString(1, email);

            ResultSet resultSet = pstmt.executeQuery();
            int userUID = 0; // Initialize the user UID
            if (resultSet.next()) {
                userUID = resultSet.getInt("UID");
            }

            // Prepare SQL query to insert reservation data into the database
            String insertQuery = "INSERT INTO Reservation (name, phone_number, email, persons, reservation_date, UID) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setInt(4, persons);
            pstmt.setString(5, reservationDate);
            pstmt.setInt(6, userUID);

            // Execute the insert query
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Data inserted successfully
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Booking confirmed');");
                out.println("window.history.back();"); // Go back to the previous page
                out.println("</script>");
            } else {
                // Handle the case where data insertion fails
                response.sendRedirect("error.jsp"); // Redirect to an error page
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
