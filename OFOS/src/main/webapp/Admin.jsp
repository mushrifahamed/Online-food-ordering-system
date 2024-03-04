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
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/UprofileStyle.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container light-style flex-grow-1 container-p-y">
    <h4 class="font-weight-bold py-3 mb-4">Admin Dashboard</h4>
        <div class="row">
            <nav id="sidebar" class="col-md-3 pt-0">
                <div class="list-group list-group-flush account-settings-links">
                    <a class="list-group-item list-group-item-action active" data-toggle="list" href="#products">Products</a>
                    <a class="list-group-item list-group-item-action" data-toggle="list" href="#add-product">Add Product</a>
                </div>
            </nav>
            <div class="col-md-9">
                <div class="tab-content">
                    <!-- Products Tab -->
                    <div class="tab-pane fade active show" id="products">
                        <h4 class="font-weight-bold py-3 mb-4">Product Management</h4>
                        <div class="form-group">
                            <!-- Search bar with search icon -->
                            <form method="post" action="">
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="Search by Product ID" name="searchProductID">
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="submit"><i class="fas fa-search"></i></button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <!-- Product table with remove button for each row -->
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Product ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Image Location</th>
                                    <th>Remove</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    // Database connection parameters
                                    String url = "jdbc:mysql://localhost:3306/ofos";
                                    String user = "root";
                                    String password = "plb123";

                                    Connection connection = null;

                                    try {
                                        Class.forName("com.mysql.cj.jdbc.Driver");
                                        connection = DriverManager.getConnection(url, user, password);

                                        String searchProductID = request.getParameter("searchProductID");

                                        // Fetch products from the database based on the searchProductID
                                        String query = "SELECT * FROM Product";
                                        if (searchProductID != null && !searchProductID.isEmpty()) {
                                            query += " WHERE ProductID = ?";
                                        }
                                        
                                        PreparedStatement statement = connection.prepareStatement(query);
                                        if (searchProductID != null && !searchProductID.isEmpty()) {
                                            statement.setInt(1, Integer.parseInt(searchProductID));
                                        }

                                        ResultSet rs = statement.executeQuery();

                                        while (rs.next()) {
                                %>
                                <tr>
                                    <td><%= rs.getInt("ProductID") %></td>
                                    <td><%= rs.getString("Name") %></td>
                                    <td><%= rs.getString("Description") %></td>
                                    <td><%= rs.getBigDecimal("Price") %></td>
                                    <td><%= rs.getString("ImageLocation") %></td>
                                    <td>
                                        <form method="post" action="RemoveProduct">
                                            <input type="hidden" name="productID" value="<%= rs.getInt("ProductID") %>">
                                            <button type="submit" class="btn btn-danger">Remove</button>
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

                    <!-- Add Product Tab -->
                    <div class="tab-pane fade" id="add-product">
                        <!-- Form for adding a new product -->
                        <form method="post" action="AddProductServlet">
                            <div class="form-group">
                                <label class="form-label">Name</label>
                                <input type="text" class="form-control" name="name">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Description</label>
                                <textarea class="form-control" name="description"></textarea>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Price</label>
                                <input type="text" class="form-control" name="price">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Image Location</label>
                                <input type="text" class="form-control" name="imageLocation">
                            </div>
                            <div class="text-right mt-3">
                                <button type="submit" class="btn btn-primary">Add Product</button>
                            </div>
                        </form>
                    </div>
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
