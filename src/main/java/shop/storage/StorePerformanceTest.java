package shop.storage;

import java.util.List;

import shop.Customer;
import shop.generator.CustomerDatabase;

public class StorePerformanceTest {

    public static void main(String[] args) {

        CustomerDatabase customerDatabase = new CustomerDatabase(10, 10, 5);
        List<Customer> customers = customerDatabase.getCustomersList();

        System.out.println("Testing KVStoreImpl...");
        KVStoreImpl kvStore = new KVStoreImpl();
        kvStore.open();
        long kvStartTime = System.currentTimeMillis();
        for (Customer customer : customers) {
            kvStore.insertCustomer(customer);
        }
        long kvEndTime = System.currentTimeMillis();
        kvStore.close();
        System.out.println("KVStoreImpl insertion time: " + (kvEndTime - kvStartTime) + " ms");

        System.out.println("Testing H2StoreImpl...");
        H2StoreImpl h2Store = new H2StoreImpl();
        h2Store.open();
        long h2StartTime = System.currentTimeMillis();
        for (Customer customer : customers) {
            h2Store.insertCustomer(customer);
        }
        long h2EndTime = System.currentTimeMillis();
        h2Store.close();
        System.out.println("H2StoreImpl insertion time: " + (h2EndTime - h2StartTime) + " ms");

        System.out.println("Results:");
        System.out.println("KVStoreImpl Time: " + (kvEndTime - kvStartTime) + " ms");
        System.out.println("H2StoreImpl Time: " + (h2EndTime - h2StartTime) + " ms");
    }
}
