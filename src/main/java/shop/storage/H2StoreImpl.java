package shop.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import shop.Customer;
import shop.Order;
import shop.Product;

// TODO: Task 2.2 c)
public class H2StoreImpl implements CustomerStore, CustomerStoreQuery {
    private static Connection dbConnection;
        
        @Override
        public void open() {
            // TODO
            try {
                dbConnection = DriverManager.getConnection("jdbc:h2:./customerDB", "sa", "");
                createTables();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Error: Unable to connect to database");
            }
            
        }
    
        private static void createTables() throws SQLException {
            // TODO
    
            String createCustomer =
                    "CREATE TABLE IF NOT EXISTS CUSTOMERS(id int primary key, name varchar(255))";
            String createOrder = "CREATE TABLE IF NOT EXISTS ORDERS(" +
                    "oid INT PRIMARY KEY, " +
                    "shippingAddress VARCHAR(255)";
            String createProduct =
                    "CREATE TABLE IF NOT EXISTS PRODUCTS(pid int primary key, pname varchar(255))";
            String createOrderItem =
                    "CREATE TABLE IF NOT EXISTS ORDERITEMS(orderId int auto_increment, orderId int, productId int)";
    
            try (Statement commandStatement = dbConnection.createStatement()) {
                commandStatement.execute(createCustomer);
                commandStatement.execute(createOrder);
                commandStatement.execute(createProduct);
                commandStatement.execute(createOrderItem);
        }
    }

    @Override
    public void insertCustomer(Customer customer) {
        // TODO
        try {

            String insertCustomerSQL = "INSERT INTO CUSTOMERS (id, name) VALUES (?, ?)";
            try (PreparedStatement customerStmt = dbConnection.prepareStatement(insertCustomerSQL)) {
                customerStmt.setInt(1, customer.getCustomerId());
                customerStmt.setString(2, customer.getUserName());
                customerStmt.executeUpdate();
            }

            for (Order order : customer.getOrders()) {

                String insertOrderSQL = "INSERT INTO ORDERS (oid, shippingAddress) VALUES (?, ?)";
                try (PreparedStatement orderStmt = dbConnection.prepareStatement(insertOrderSQL)) {
                    orderStmt.setInt(1, order.getOrderId());
                    orderStmt.setString(2, order.getShippingAddress());
                    orderStmt.executeUpdate();
                }

                for (Product product : order.getItems()) {
                    
                    String insertProductSQL = "MERGE INTO PRODUCTS (pid, pname) KEY(pid) VALUES (?, ?)";
                    try (PreparedStatement productStmt = dbConnection.prepareStatement(insertProductSQL)) {
                        productStmt.setInt(1, product.getProductId());
                        productStmt.setString(2, product.getName());
                        productStmt.executeUpdate();
                    }

                    String insertOrderItemSQL = "INSERT INTO ORDERITEMS (orderId, productId) VALUES (?, ?)";
                    try (PreparedStatement orderItemStmt = dbConnection.prepareStatement(insertOrderItemSQL)) {
                        orderItemStmt.setInt(1, order.getOrderId());
                        orderItemStmt.setInt(2, product.getProductId());
                        orderItemStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting customer into H2 database.");
        }
    }

    @Override
    public void close() {
        // TODO
        try {
            dbConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Unable to close database connection.");
        }
    }

    @Override
    public void cleanUp() {
        // TODO
        try (Statement commandStatement = dbConnection.createStatement()) {
            commandStatement.execute("DROP TABLE CUSTOMERS");
            commandStatement.execute("DROP TABLE ORDERS");
            commandStatement.execute("DROP TABLE PRODUCTS");
            commandStatement.execute("DROP TABLE ORDERITEMS");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to drop tables.");
        }
    }

    @Override
    public void queryAllUsers() {
        // TODO
        try (Statement commandStatement = dbConnection.createStatement()) {
            ResultSet executeResult = commandStatement.executeQuery("SELECT * FROM CUSTOMERS");
            while (executeResult.next()) {
                System.out.println("Customer ID: " + executeResult.getInt("id"));
                System.out.println("Name: " + executeResult.getString("name"));
                System.out.println("Address: " + executeResult.getString("address"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying all users from H2 database.");
        }
    }

    @Override
    public void queryTopProduct() {
        // TODO
        String query = "SELECT PRODUCTS.pid, PRODUCTS.pname, COUNT(ORDERITEMS.productId) AS order_count " +
                       "FROM ORDERITEMS JOIN PRODUCTS ON ORDERITEMS.productId = PRODUCTS.pid " +
                       "GROUP BY PRODUCTS.pid, PRODUCTS.pname " +
                       "ORDER BY order_count DESC LIMIT 1";
        try (Statement commandStatement = dbConnection.createStatement()) {
            ResultSet executeResult = commandStatement.executeQuery(query);
            while (executeResult.next()) {
                System.out.println("Top Product details are as follows:");
                System.out.println("Product ID: " + executeResult.getInt("pid"));
                System.out.println("Product Name: " + executeResult.getString("pname"));
                System.out.println("Order Count: " + executeResult.getInt("order_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error querying top product from database.");
        }
    }
}
