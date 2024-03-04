package UpdateServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

@WebServlet("/UpdateOrderStatusServlet")
public class UpdateOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderID = request.getParameter("orderID");
        String newStatus = request.getParameter("newStatus");

        Connection connection = null;
        try {
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/ofos";
            String user = "root";
            String password = "plb123";

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            String updateQuery = "UPDATE Orders SET OrderStatus = ? WHERE OrderID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, Integer.parseInt(orderID));

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Order status updated successfully
                response.getWriter().write("success");
            } else {
                // Order status update failed
                response.getWriter().write("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error");
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