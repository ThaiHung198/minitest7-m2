package storage;

import model.Order;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileStorage implements OrderStorage {

    private final String filename;

    public BinaryFileStorage(String filename) {
        // Gán tên file, đảm bảo không null/rỗng (có thể thêm default)
        this.filename = (filename == null || filename.trim().isEmpty()) ? "orders_data.bin" : filename.trim();
    }

    @Override
    public void saveOrders(List<Order> orders) throws IOException { // Ném IOException
        // Đảm bảo không lưu list null
        List<Order> listToSave = (orders == null) ? new ArrayList<>() : orders;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(listToSave);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> loadOrders() throws IOException, ClassNotFoundException { // Ném các Exception liên quan
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // Trả về rỗng nếu file không tồn tại hoặc trống
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Order>) obj;
            } else {
                // Nếu file không chứa List, coi như lỗi định dạng
                throw new IOException("Định dạng tệp không hợp lệ: Dự kiến một Danh sách trong " + filename);
            }
        } catch (EOFException e) {
            // File bị cắt cụt hoặc trống sau khi đã tồn tại
            System.err.println("Cảnh báo: Phát hiện tệp trống hoặc bị hỏng: " + filename);
            return new ArrayList<>();
        }
    }
}