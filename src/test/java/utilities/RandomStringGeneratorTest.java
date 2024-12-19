package utilities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RandomStringGenerator class.
 */
class RandomStringGeneratorTest {

    /**
     * Tests that the generated string has the correct length.
     */
    @Test
    void testGenerateRandomString_Length() {
        int desiredLength = 10;
        String randomString = RandomStringGenerator.generateRandomString(desiredLength);

        // Assertions
        assertNotNull(randomString, "The generated string should not be null.");
        assertEquals(desiredLength, randomString.length(), "The generated string should have the specified length.");
    }

    /**
     * Tests that the generated string only contains valid alphanumeric characters.
     */
    @Test
    void testGenerateRandomString_ValidCharacters() {
        String randomString = RandomStringGenerator.generateRandomString(50);

        // Define the valid character set.
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Assert that each character in the generated string is within the valid character set.
        for (char c : randomString.toCharArray()) {
            assertTrue(validCharacters.indexOf(c) >= 0, "The character '" + c + "' is not valid.");
        }
    }

    /**
     * Tests that two generated strings are not identical.
     * This ensures a basic level of randomness.
     */
    @Test
    void testGenerateRandomString_Uniqueness() {
        String string1 = RandomStringGenerator.generateRandomString(20);
        String string2 = RandomStringGenerator.generateRandomString(20);

        // Assertions
        assertNotEquals(string1, string2, "Two generated strings should not be identical.");
    }

    /**
     * Tests that the generator can handle edge cases like generating an empty string.
     */
    @Test
    void testGenerateRandomString_EmptyString() {
        String randomString = RandomStringGenerator.generateRandomString(0);

        // Assertions
        assertNotNull(randomString, "The generated string should not be null.");
        assertEquals(0, randomString.length(), "The generated string should have a length of zero.");
    }

    /**
     * Tests the randomness of the generator by ensuring that it can produce a diverse set of strings.
     */
    @Test
    void testGenerateRandomString_Randomness() {
        int length = 15;
        int sampleSize = 1000;
        Set<String> generatedStrings = new HashSet<>();

        // Generate a large number of random strings and store them in a set to check for uniqueness.
        for (int i = 0; i < sampleSize; i++) {
            String randomString = RandomStringGenerator.generateRandomString(length);
            generatedStrings.add(randomString);
        }

        // Assertions
        assertEquals(sampleSize, generatedStrings.size(), "The generator should produce unique strings.");
    }

    /**
     * Tests the behaviour when a negative length is provided.
     * This ensures that the method handles invalid input gracefully.
     */
    @Test
    void testGenerateRandomString_NegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            RandomStringGenerator.generateRandomString(-1);
        }, "Generating a string with a negative length should throw an exception.");
    }
}
