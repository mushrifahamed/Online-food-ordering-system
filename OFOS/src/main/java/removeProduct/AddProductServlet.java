package removeProduct;

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

@WebServlet("/AddProductServlet")
public class AddProductServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String imageLocation = request.getParameter("imageLocation");

        if (name != null && description != null && priceStr != null && imageLocation != null) {
            try {
                double price = Double.parseDouble(priceStr);
                String url = "jdbc:mysql://localhost:3306/ofos";
                String user = "root";
                String password = "plb123";

                Connection connection = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection(url, user, password);

                    String query = "INSERT INTO Product (Name, Description, Price, ImageLocation) VALUES (?, ?, ?, ?)";

                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, name);
                    statement.setString(2, description);
                    statement.setDouble(3, price);
                    statement.setString(4, imageLocation);

                    statement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        try {
							connection.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }

                response.sendRedirect("AdminDashboard.jsp"); // Redirect back to the admin dashboard page
            } catch (NumberFormatException e) {
                // Handle the case where price is not a valid number
                response.sendRedirect("Admin.jsp");
            }
        } else {
            // Handle the case where form fields are missing
            response.sendRedirect("Admin.jsp");
        }
    }
}
