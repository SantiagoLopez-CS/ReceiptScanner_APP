package papertrail.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;

public class JsonTimeTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Enable Java 8+ time support

        // Example object with LocalDate
        Example obj = new Example("Test", LocalDate.now());

        // Serialize to JSON
        String json = mapper.writeValueAsString(obj);
        System.out.println("Serialized: " + json);

        // Deserialize back
        Example readBack = mapper.readValue(json, Example.class);
        System.out.println("Deserialized: " + readBack);
    }

    // Simple class with a LocalDate field
    public static class Example {
        public String name;
        public LocalDate date;

        // Default constructor required for Jackson
        public Example() {}

        public Example(String name, LocalDate date) {
            this.name = name;
            this.date = date;
        }

        @Override
        public String toString() {
            return "Example{name='" + name + "', date=" + date + "}";
        }
    }
}
