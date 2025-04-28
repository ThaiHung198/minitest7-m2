package storage;

import model.Order;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class OrderStorage {

    private final String filename;


    public OrderStorage(String filename) {
        this.filename = (filename == null || filename.trim().isEmpty()) ? "default_orders.dat" : filename.trim();
        System.out.println("INFO: OrderStorage initialized with file: " + this.filename);
    }

    public void saveOrders(List<Order> orders) {
        List<Order> listToSave = (orders == null) ? new ArrayList<>() : orders;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(listToSave);
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not save data to " + filename + " - " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    public List<Order> loadOrders() {
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject(); // Đọc đối tượng từ file
            if (obj instanceof List) {
                // Giả định rằng List chứa các đối tượng Order
                List<Order> loadedOrders = (List<Order>) obj;
                return loadedOrders;
            } else {
                System.err.println("ERROR: File '" + filename + "' contains invalid data format (not a List).");
                return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: File not found during read (unexpected): " + filename);
            return new ArrayList<>();
        } catch (EOFException e) {
            System.err.println("ERROR: Reached end of file unexpectedly for '" + filename + "'. File might be corrupted.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("ERROR: Failed to load or parse data from '" + filename + "': " + e.getMessage());
            return new ArrayList<>(); // Trả về rỗng khi có lỗi
        }
    }
}