import shop.Customer;
import shop.generator.ShopGenerator;
import shop.storage.H2StoreImpl;
import shop.storage.KVStoreImpl;

public class InsertionPerformanceTest {
    private static final int NUM_CUSTOMERS = 9999;

    public static void main(String[] args) {

        System.out.println("Generating test data...");
        var products = ShopGenerator.generateProducts(100);
        var customers = new Customer[NUM_CUSTOMERS];
        for (int i = 0; i < NUM_CUSTOMERS; i++) {
            customers[i] = ShopGenerator.generateCustomer(products, 5);
        }

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

        System.out.println("Performance Results:");
        System.out.println("KVStoreImpl Time: " + (kvEndTime - kvStartTime) + " ms");
        System.out.println("H2StoreImpl Time: " + (h2EndTime - h2StartTime) + " ms");
    }
}
