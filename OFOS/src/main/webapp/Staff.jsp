<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>

<% 
if (session.getAttribute("email") == null) {
    response.sendRedirect("Login.jsp");
}
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Dashboard</title>
    <link rel="stylesheet" href="css/UprofileStyle.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="container light-style flex-grow-1 container-p-y">
        <h4 class="font-weight-bold py-3 mb-4">Staff Dashboard</h4>
        <div class="row">
            <nav id="sidebar" class="col-md-3 pt-0">
                <div class="list-group list-group-flush account-settings-links">
                    <a class="list-group-item list-group-item-action active" data-toggle="list" href="#orders">Orders</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" href="#add-order">Add Order</a>
                </div>
            </nav>
            <div class="col-md-9">
                <div class="tab-content">
                    <!-- Orders Tab -->
					<div class="tab-pane fade active show" id="orders">
					    <h4 class="font-weight-bold py-3 mb-4">Order Management</h4>
					    <div class="form-group">
					        <!-- Search bar with search icon -->
					        <form method="post" action="">
					            <div class="input-group">
					                <input type="text" class="form-control" placeholder="Search by Order ID" name="searchOrderID">
					                <div class="input-group-append">
					                    <button class="btn btn-primary" type="submit"><i class="fas fa-search"></i></button>
					                </div>
					            </div>
					        </form>
					    </div>
					    <!-- Order table with remove button and status update for each row -->
					    <table class="table table-striped">
					        <thead>
					            <tr>
					                <th>Order ID</th>
					                <th>UID (Order Placer)</th>
					                <th>Delivery Address</th>
					                <th>Phone Number</th>
					                <th>Order Status</th>
					                <th>Remove</th>
					            </tr>
					        </thead>
					        <tbody>
					            <%
					                String url = "jdbc:mysql://localhost:3306/ofos";
					                String user = "root";
					                String password = "plb123";
					
					                Connection connection = null;
					
					                try {
					                    Class.forName("com.mysql.cj.jdbc.Driver");
					                    connection = DriverManager.getConnection(url, user, password);
					
					                    String searchOrderID = request.getParameter("searchOrderID");
					
					                    String query = "SELECT * FROM Orders";
					                    if (searchOrderID != null && !searchOrderID.isEmpty()) {
					                        query += " WHERE OrderID = ?";
					                    }
					
					                    PreparedStatement statement = connection.prepareStatement(query);
					                    if (searchOrderID != null && !searchOrderID.isEmpty()) {
					                        statement.setInt(1, Integer.parseInt(searchOrderID));
					                    }
					
					                    ResultSet rs = statement.executeQuery();
					
					                    while (rs.next()) {
					                        String orderStatus = rs.getString("OrderStatus");
					            %>
					            <tr>
								    <td><%= rs.getInt("OrderID") %></td>
								    <td><%= rs.getInt("UID") %></td>
								    <td><%= rs.getString("DeliveryAddress") %></td>
								    <td><%= rs.getString("PhoneNumber") %></td>
								    <td>
								        <form method="post" action="UpdateOrderStatusServlet">
								            <input type="hidden" name="orderID" value="<%= rs.getInt("OrderID") %>">
								            <select name="newStatus" class="order-status">
								                <option value="Pending" <%= orderStatus.equals("Pending") ? "selected" : "" %>>Pending</option>
								                <option value="Complete" <%= orderStatus.equals("Complete") ? "selected" : "" %>>Complete</option>
								                <option value="Cancelled" <%= orderStatus.equals("Cancelled") ? "selected" : "" %>>Cancelled</option>
								            </select>
								            <button type="submit" class="btn btn-primary">Update</button>
								        </form>
								    </td>
								    <td>
								        <form method="post" action="RemoveOrderServlet">
								            <input type="hidden" name="orderID" value="<%= rs.getInt("OrderID") %>">
								            <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Remove</button>
								        </form>
								    </td>
								</tr>
					            <%
					                }
					            } catch (Exception e) {
					                e.printStackTrace();
					            } finally {
					                if (connection != null) {
					                    connection.close();
					                }
					            }
					            %>
					        </tbody>
					    </table>
					</div>

					<!-- Add Order Tab -->
					<div class="tab-pane fade" id="add-order">
					    <!-- Form for adding a new order with multiple items -->
					    <form method="post" action="AddOrderServlet">
					        <div class="form-group">
					            <label class="form-label">Delivery Address</label>
					            <input type="text" class="form-control" name="deliveryAddress">
					        </div>
					        <div class="form-group">
					            <label class="form-label">Phone Number</label>
					            <input type="text" class="form-control" name="phoneNumber">
					        </div>
					        <div class="form-group">
					            <label class="form-label">Order Status</label>
					            <input type="text" class="form-control" name="orderStatus" value="Pending" disabled>
					        </div>
					        <div class="form-group">
					            <label class="form-label">Items (Product and Quantity)</label>
					            <div id="items-container">
					                <div class="item-row">
					                    <select name="productID[]" class="form-control">
					                        <option value="">Select Product</option>
					                        <%
					                            String url1 = "jdbc:mysql://localhost:3306/ofos";
					                            String user1 = "root";
					                            String password1 = "plb123";
					
					                            Connection connection1 = null;
					
					                            try {
					                                Class.forName("com.mysql.cj.jdbc.Driver");
					                                connection1 = DriverManager.getConnection(url1, user1, password1);
					
					                                String query = "SELECT ProductID, Name FROM Product";
					                                PreparedStatement statement = connection1.prepareStatement(query); // Changed connection to connection1
					                                ResultSet rs = statement.executeQuery();
					
					                                while (rs.next()) {
					                        %>
					                        <option value="<%= rs.getInt("ProductID") %>"><%= rs.getString("Name") %></option>
					                        <%
					                            }
					                        } catch (Exception e) {
					                            e.printStackTrace();
					                        } finally {
					                            if (connection1 != null) { // Changed connection to connection1
					                                connection1.close(); // Changed connection to connection1
					                            }
					                        }
					                        %>
					                    </select>
					                    <input type="text" class="form-control" name="quantity[]" placeholder="Quantity">
					                    <button type="button" class="btn btn-danger remove-item">Remove Item</button>
					                </div>
					            </div>
					            <button type="button" class="btn btn-primary" id="add-item">Add Item</button>
					        </div>
					        <div class="text-right mt-3">
					            <button type="submit" class="btn btn-primary">Add Order</button>
					        </div>
					    </form>
					</div>
					
					<!-- JavaScript to add and remove item rows -->
					<script>
					    $(document).ready(function() {
					        $("#add-item").click(function() {
					            // Clone the first item row and append it to the container
					            var newItem = $(".item-row:first").clone();
					            $("#items-container").append(newItem);
					            newItem.find("input").val(""); // Clear input values in the cloned row
					        });
					
					        // Remove item row when "Remove Item" button is clicked
					        $("#items-container").on("click", ".remove-item", function() {
					            if ($(".item-row").length > 1) {
					                $(this).closest(".item-row").remove();
					            }
					        });
					    });
					</script>
                </div>
            </div>
        </div>
        <!-- Logout button -->
        <div class="text-right mt-3">
            <form method="post" action="Logout">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </div>

    <!-- Include necessary JavaScript libraries -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>