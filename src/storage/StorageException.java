package storage;

public class StorageException extends Exception {

    // Constructor nhận vào một thông điệp lỗi
    public StorageException(String message) {
        super(message); // Gọi constructor của lớp cha (Exception)
    }

    // Constructor nhận vào thông điệp lỗi và nguyên nhân gốc (lỗi gốc)
    public StorageException(String message, Throwable cause) {
        super(message, cause); // Gọi constructor của lớp cha
    }
}