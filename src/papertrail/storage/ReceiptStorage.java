package papertrail.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import papertrail.model.Receipt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptStorage {

    private static final String FILE_PATH = "data/receipts.json";
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    public static void saveReceipts(List<Receipt> receipts) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, receipts);
        } catch (IOException e) {
            System.err.println("Error saving receipts: " + e.getMessage());
        }
    }

    public static List<Receipt> loadReceipts() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return mapper.readValue(file, new TypeReference<List<Receipt>>() {});
        } catch (IOException e) {
            System.err.println("Error loading receipts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
