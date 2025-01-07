package utilities;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating random strings.
 * This class uses a secure random number generator to produce strings composed of alphanumeric characters.
 */
public class RandomStringGenerator {

    // The set of characters that can be used to generate random strings.
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // SecureRandom is used to ensure a higher level of randomness and security compared to Random.
    private static final SecureRandom RANDOM = new SecureRandom();

    // A list containing all the previously generated strings.
    private static final List<String> generatedStrings = new ArrayList<String>();

    /**
     * Generates a random string of the specified length without considering the string's uniqueness.
     * The string will consist of uppercase and lowercase letters, as well as digits.
     *
     * @param length The desired length of the random string.
     * @return A randomly generated string of the specified length.
     */
    private static String generateRawRandomString(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative.");
        }

        // StringBuilder is used for efficient string construction.
        StringBuilder sb = new StringBuilder(length);

        // Append random characters from the CHARACTERS string until the desired length is reached.
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        // Return the generated random string.
        return sb.toString();
    }

    /**
     * Generates a random string of the specified length.
     * The string will consist of uppercase and lowercase letters, as well as digits.
     *
     * @param length The desired length of the random string.
     * @param isUnique Whether the random string generated is unique or not.
     * @return A randomly generated string of the specified length.
     */
    public static String generateRandomString(int length, boolean isUnique) {
        while (true) {
            String randomString = generateRawRandomString(length);
            if (isUnique && !generatedStrings.contains(randomString)) {
                generatedStrings.add(randomString);
                return randomString;
            }
        }
    }

    /**
     * Generates a random unique string of the specified length.
     * The string will consist of uppercase and lowercase letters, as well as digits.
     *
     * @param length The desired length of the random string.
     * @return A randomly generated string of the specified length.
     */
    public static String generateRandomString(int length) {
        return generateRandomString(length, true);
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RandomStringGenerator() {
        // This class should not be instantiated.
    }
}
