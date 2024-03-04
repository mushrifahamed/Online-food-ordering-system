package removeProduct;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/RemoveProduct")
public class RemoveServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the product ID to be removed from the request parameter
        String productID = request.getParameter("productID");

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/ofos";
        String user = "root";
        String password = "plb123";

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            // Construct the SQL query to delete the product
            String deleteQuery = "DELETE FROM Product WHERE ProductID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, Integer.parseInt(productID));

            // Execute the delete query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Product successfully removed
                response.sendRedirect("Admin.jsp");
            } else {
                // Handle the case where the product was not found or couldn't be removed
                // You can display an error message or take other appropriate action
                response.sendRedirect("Admin.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the removal process
            response.sendRedirect("Admin.jsp");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
