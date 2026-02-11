package papertrail.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import papertrail.model.Budget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BudgetStorage {

    private static final String FILE_PATH = "data/budgets.json";
    private static final ObjectMapper mapper= new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    public static void saveBudgets(List<Budget> budgets) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // Ensure/data exists
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, budgets);
        } catch (IOException e) {
            System.err.println("Error saving budgets: " + e.getMessage());
        }
    }

    public static List<Budget> loadBudgets() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return mapper.readValue(file, new TypeReference<List<Budget>>() {});
        } catch (IOException e) {
            System.err.println("Error loading budgets: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
