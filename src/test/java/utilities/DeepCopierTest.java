package utilities;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DeepCopier class.
 */
class DeepCopierTest {

    /**
     * Tests the deep copy of a simple object (e.g., a String).
     */
    @Test
    void testDeepCopySimpleObject() {
        String original = "Hello, World!";
        String copy = DeepCopier.deepCopy(original, String.class);

        // Assertions
        assertEquals(original, copy, "The content of the deep copy should match the original.");
        assertNotSame(original, copy, "The deep copy should not reference the same object as the original.");
    }

    /**
     * Tests the deep copy of a complex object.
     */
    @Test
    void testDeepCopyComplexObject() {
        Person original = new Person("John Doe", 30);
        Person copy = DeepCopier.deepCopy(original, Person.class);

        // Assertions
        assertEquals(original, copy, "The deep copy of the complex object should match the original.");
        assertNotSame(original, copy, "The deep copy of the complex object should not reference the same object.");
    }

    /**
     * Tests the deep copy of a list of objects.
     */
    @Test
    void testDeepCopyList() {
        List<String> original = Arrays.asList("one", "two", "three");
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> copy = DeepCopier.deepCopy(original, listType);

        // Assertions
        assertEquals(original, copy, "The deep copy of the list should match the original.");
        assertNotSame(original, copy, "The deep copy of the list should not reference the same object.");
    }

    /**
     * Tests the deep copy of a map of objects.
     */
    @Test
    void testDeepCopyMap() {
        Map<String, Integer> original = new HashMap<>();
        original.put("key1", 1);
        original.put("key2", 2);

        Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
        Map<String, Integer> copy = DeepCopier.deepCopy(original, mapType);

        // Assertions
        assertEquals(original, copy, "The deep copy of the map should match the original.");
        assertNotSame(original, copy, "The deep copy of the map should not reference the same object.");
    }

    /**
     * Tests the deep copy of a nested object structure.
     */
    @Test
    void testDeepCopyNestedObject() {
        Organisation original = new Organisation("Company", Arrays.asList(new Person("Alice", 25), new Person("Bob", 35)));
        Organisation copy = DeepCopier.deepCopy(original, Organisation.class);

        // Assertions
        assertEquals(original, copy, "The deep copy of the nested object should match the original.");
        assertNotSame(original, copy, "The deep copy of the nested object should not reference the same object.");
    }

    // Helper classes for testing

    private static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    private static class Organisation { // Changed from 'Organization' to 'Organisation'
        private String name;
        private List<Person> employees;

        public Organisation(String name, List<Person> employees) {
            this.name = name;
            this.employees = employees;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Organisation that = (Organisation) o;
            return Objects.equals(name, that.name) && Objects.equals(employees, that.employees);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, employees);
        }
    }
}
