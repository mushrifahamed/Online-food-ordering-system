package UpdateServlet;

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

@WebServlet("/AddOrderServlet")
public class AddOrder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the input fields from the form
        String deliveryAddress = request.getParameter("deliveryAddress");
        String phoneNumber = request.getParameter("phoneNumber");
        String orderStatus = "Pending"; // Set the order status to "Pending"
        String[] productIDs = request.getParameterValues("productID[]");
        String[] quantities = request.getParameterValues("quantity[]");

        Connection connection = null;

        try {
            String url = "jdbc:mysql://localhost:3306/ofos";
            String user = "root";
            String password = "plb123";

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            // First, insert the order into the Orders table
            String insertOrderQuery = "INSERT INTO Orders (UID, DeliveryAddress, PhoneNumber, OrderStatus) VALUES (?, ?, ?, ?)";
            
            // Replace 'uid' with the actual user's ID from the session
            int uid = 8; // Replace with the user's actual ID
            
            PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery);
            orderStatement.setInt(1, uid);
            orderStatement.setString(2, deliveryAddress);
            orderStatement.setString(3, phoneNumber);
            orderStatement.setString(4, orderStatus);
            
            int orderInserted = orderStatement.executeUpdate();
            
            if (orderInserted > 0) {
                // Order inserted successfully, now insert the order items
                String selectOrderIDQuery = "SELECT LAST_INSERT_ID() as LastID"; // Retrieve the last inserted OrderID
                
                PreparedStatement lastIDStatement = connection.prepareStatement(selectOrderIDQuery);
                ResultSet lastIDResult = lastIDStatement.executeQuery();
                
                int orderID = 0;
                if (lastIDResult.next()) {
                    orderID = lastIDResult.getInt("LastID");
                }
                
                // Insert the order items
                String insertOrderItemsQuery = "INSERT INTO OrderItems (OrderID, ProductID, Quantity) VALUES (?, ?, ?)";
                PreparedStatement orderItemsStatement = connection.prepareStatement(insertOrderItemsQuery);
                
                for (int i = 0; i < productIDs.length; i++) {
                    int productID = Integer.parseInt(productIDs[i]);
                    int quantity = Integer.parseInt(quantities[i]);
                    
                    orderItemsStatement.setInt(1, orderID);
                    orderItemsStatement.setInt(2, productID);
                    orderItemsStatement.setInt(3, quantity);
                    
                    orderItemsStatement.addBatch();
                }
                
                int[] itemsInserted = orderItemsStatement.executeBatch();
                
                boolean allItemsInserted = true;
                for (int inserted : itemsInserted) {
                    if (inserted <= 0) {
                        allItemsInserted = false;
                        break;
                    }
                }
                
                if (allItemsInserted) {
                    // All items inserted successfully
                    response.sendRedirect("Staff.jsp"); // Redirect to a success page or dashboard
                } else {
                    // Handle error if not all items were inserted
                    response.getWriter().write("Failed to insert order items.");
                }
            } else {
                // Handle error if order insertion failed
                response.getWriter().write("Failed to insert order.");
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