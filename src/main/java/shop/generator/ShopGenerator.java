package shop.generator;

import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;

import shop.Customer;
import shop.Order;
import shop.Product;

// TODO: Task 2.2 a)
public class ShopGenerator {
    public static List<Product> generateProducts(int numProducts) {
        // TODO
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= numProducts; i++) {
            Faker newRandomGenerator = new Faker();
            int productID = i * newRandomGenerator.number().numberBetween(1000, 9999);
            String productName = newRandomGenerator.food().ingredient();
            products.add(new Product(productID, productName));
        }
        return products;
    }

    public static List<Order> generateOrders(List<Product> products, int numOrders) {
        // TODO
        List<Order> orders = new ArrayList<>();
        for(int i = 1; i <= numOrders; i++) {
            Faker newRandomGenerator = new Faker();
            int orderID = i * newRandomGenerator.number().numberBetween(99999, 894282);
            String shippingAddress = newRandomGenerator.address().streetAddress();
            List<Product> orderItems = new ArrayList<>();
            for(int j = 0; j < newRandomGenerator.number().numberBetween(1, products.size()/2); j++){
                orderItems.add(products.get(newRandomGenerator.number().numberBetween(0, products.size())));
            }
            orders.add(new Order(orderID, shippingAddress, orderItems));
        }
        return orders;
    }

    public static Customer generateCustomer(List<Product> products, int maxOrders) {
        // TODO
        Faker newRandomGenerator = new Faker();
        int customerID = newRandomGenerator.number().numberBetween(9999, 99999);
        String userName = newRandomGenerator.name().username();
        String address = newRandomGenerator.address().streetAddress();

        Customer customer = new Customer(customerID, userName, address);
        
        List<Order> order = generateOrders(products, newRandomGenerator.number().numberBetween(1, maxOrders));
        for(Order o : order){
            customer.addOrder(o);
        }

        return customer;
    }

}
