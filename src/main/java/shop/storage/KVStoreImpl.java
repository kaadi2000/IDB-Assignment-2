package shop.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import shop.Customer;
import shop.Order;
import shop.Product;

// TODO: Task 2.2 b)
public class KVStoreImpl implements CustomerStore, CustomerStoreQuery {
    private RecordManager recordManager;
    private PrimaryHashMap<Integer, Customer> customerMap;
    
    @Override
    public void open() {
        try {
            // TODO
            recordManager = RecordManagerFactory.createRecordManager("shop");
            customerMap = recordManager.hashMap("customerMap");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error: Unable to initialize record manager");
        }

    }

    @Override
    public void insertCustomer(Customer customer) {
        // TODO
        if(customerMap != null){
            customerMap.put(customer.getCustomerId(), customer);
            try {
                recordManager.commit();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Error: Unable to commit record manager.");
            }
        }
    }

    @Override
    public void close() {
        // TODO
        if(customerMap != null){
            try {
                recordManager.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Error during cleanup");
            }
        }
    }

    @Override
    public void cleanUp() {
        // TODO
        if(customerMap != null){
            customerMap.clear();
            try {
                recordManager.commit();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Error during cleanup");
            }
        }
    }

    @Override
    public void queryAllUsers() {
        // TODO
        if(customerMap != null){
            customerMap.forEach((id, customer) -> {
                System.out.println("Customer ID: " + customer.getCustomerId());
                System.out.println("User Name: " + customer.getUserName());
                System.out.println("Address: " + customer.getAddress());
                System.out.println("Orders: " + customer.getOrders());
            });

        }
    }

    @Override
    public void queryTopProduct() {
        // TODO
        if(customerMap != null){
            Map<Product, Integer> productCount = new HashMap<>();
            
            for(Customer c: customerMap.values()){
                List<Order> orders = c.getOrders();

                for(Order o: orders){
                    List<Product> products = o.getItems();
                    
                    for(Product p: products){
                        productCount.put(p, productCount.getOrDefault(p, 0) + 1);
                    }
                }
            }

            Product topProduct = null;
            int maximumCount = 0;

            for(Entry<Product, Integer> entry : productCount.entrySet()){
                if(entry.getValue() > maximumCount){
                    maximumCount = entry.getValue();
                    topProduct = entry.getKey();
                }
            }

            if( topProduct != null){
                System.out.println("Top product details are as follows:");
                System.out.println("Product ID: " + topProduct.getProductId());
                System.out.println("Product Name: " + topProduct.getName());
            }
            else{
                System.out.print("No products can be found");
            }
        }
    }
}
