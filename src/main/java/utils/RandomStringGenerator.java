package utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for generating random alphanumeric strings.
 * Includes functionality to generate both general and unique strings.
 *
 * <p>Note: Uniqueness is determined by maintaining an in-memory list
 * of previously generated strings for the duration of the application's runtime.
 */
public abstract class RandomStringGenerator {

    // Pool of characters to be used for random string generation
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Secure random generator for producing unpredictable sequences
    private static final SecureRandom RANDOM = new SecureRandom();

    // List of all generated strings used to enforce uniqueness
    private static final List<String> generatedStrings = new ArrayList<>();

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length the desired length of the string; must be non-negative
     * @return a randomly generated alphanumeric string
     * @throws IllegalArgumentException if length is negative
     */
    public static String generateRandomString(int length) {
        if (length < 0)
            throw new IllegalArgumentException("Length must be non-negative.");

        StringBuilder sb = new StringBuilder(length);

        // Build the string one random character at a time
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        String randomString = sb.toString();

        // Store the generated string for uniqueness checking
        generatedStrings.add(randomString);

        return randomString;
    }

    /**
     * Generates a unique random alphanumeric string of the specified length.
     * If a duplicate is generated, it retries until a unique one is found.
     *
     * @param length the desired length of the string; must be non-negative
     * @return a unique randomly generated alphanumeric string
     */
    public static String generateUniqueRandomString(int length) {
        while (true) {
            String randomString = generateRandomString(length);

            // Ensure the string is unique by checking frequency in the list
            if (Collections.frequency(generatedStrings, randomString) == 1)
                return randomString;
        }
    }
}
