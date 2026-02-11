package papertrail.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // A simple object to test
            Person p = new Person("Alice", 25);

            // Serialization to JSON
            String json = mapper.writeValueAsString(p);
            System.out.println("Serialization JSON: " + json);

            // Deserialize back
            Person deserialized = mapper.readValue(json, Person.class);
            System.out.println("Deserialized Object: " + deserialized);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple helper class
    static class Person {
        public String name;
        public int age;

        // Needed for Jackson
        public Person() {}

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}
