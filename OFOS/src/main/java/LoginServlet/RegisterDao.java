package LoginServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;

public class RegisterDao {
    private String dburl = "jdbc:mysql://localhost:3306/ofos";
    private String dbuname = "root";
    private String dbpassword = "plb123";
    private String dbdriver = "com.mysql.cj.jdbc.Driver";

    public void loadDriver(String dbDriver) {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(dburl, dbuname, dbpassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public String insert(Member member, HttpServletResponse response) throws IOException {
        loadDriver(dbdriver);
        Connection con = getConnection();
        String result = "Data Entered Successfully";

        try {
            // Insert data into the Users table
            String userInsertSql = "INSERT INTO Users (email, password, type) VALUES (?, ?, 'customer')";
            PreparedStatement userStmt = con.prepareStatement(userInsertSql);
            userStmt.setString(1, member.getEmail());
            userStmt.setString(2, member.getPassword());
            userStmt.executeUpdate();

            // Retrieve the generated UID
            String selectUIDSql = "SELECT UID FROM Users WHERE email = ?";
            PreparedStatement uidStmt = con.prepareStatement(selectUIDSql);
            uidStmt.setString(1, member.getEmail());
            int UID = -1;

            // Execute the query and get the UID
            uidStmt.execute();
            var resultSet = uidStmt.getResultSet();
            if (resultSet.next()) {
                UID = resultSet.getInt("UID");
            }

            if (UID != -1) {
                // Insert data into the Customer table using the retrieved UID
                String customerInsertSql = "INSERT INTO Customer (name, email, phone_number, UID) VALUES (?, ?, ?, ?)";
                PreparedStatement customerStmt = con.prepareStatement(customerInsertSql);
                customerStmt.setString(1, member.getName());
                customerStmt.setString(2, member.getEmail());
                customerStmt.setString(3, member.getPhone());
                customerStmt.setInt(4, UID);
                customerStmt.executeUpdate();

                // Redirect to Login.jsp
                response.sendRedirect("Login.jsp");
            }
        } catch (SQLException e) {
            result = "Data Not Entered Successfully";
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
