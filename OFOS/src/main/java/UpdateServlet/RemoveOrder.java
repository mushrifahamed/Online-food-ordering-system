package UpdateServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet("/RemoveOrderServlet")
public class RemoveOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the orderID from the request parameter
        String orderID = request.getParameter("orderID");

        Connection connection = null;

        try {
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/ofos";
            String user = "root";
            String password = "plb123";

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            // Define your SQL DELETE statement to remove the order
            String deleteQuery = "DELETE FROM Orders WHERE OrderID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, Integer.parseInt(orderID));

            // Execute the DELETE statement
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                // Order removed successfully
                response.sendRedirect("Staff.jsp"); // Redirect to the admin dashboard or any other appropriate page
            } else {
                // Order removal failed
                response.getWriter().write("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}