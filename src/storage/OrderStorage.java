package storage;

import model.Order;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class OrderStorage {

    private final String filename;


    public OrderStorage(String filename) {
        this.filename = (filename == null || filename.trim().isEmpty()) ? "default_orders.dat" : filename.trim();
        System.out.println("THÔNG TIN: OrderStorage được khởi tạo bằng tệp: " + this.filename);
    }

    public void saveOrders(List<Order> orders) {
        List<Order> listToSave = (orders == null) ? new ArrayList<>() : orders;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(listToSave);
        } catch (IOException e) {
            System.err.println("LỖI: Không thể lưu dữ liệu vào " + filename + " - " + e.getMessage());
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
                System.err.println("LỖI: Tệp '" + filename + "' chứa định dạng dữ liệu không hợp lệ (không phải là Danh sách).");
                return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.err.println("LỖI: Không tìm thấy tệp trong quá trình đọc (không mong muốn): " + filename);
            return new ArrayList<>();
        } catch (EOFException e) {
            System.err.println("LỖI: Đã đến cuối tệp một cách bất ngờ '" + filename + "'. Tập tin có thể bị hỏng.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.err.println("LỖI: Không tải hoặc phân tích được dữ liệu từ '" + filename + "': " + e.getMessage());
            return new ArrayList<>(); // Trả về rỗng khi có lỗi
        }
    }
}