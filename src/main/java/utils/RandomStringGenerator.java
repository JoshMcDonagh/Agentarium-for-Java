package utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<String> generatedStrings = new ArrayList<String>();

    public static String generateRandomString(int length) {
        if (length < 0)
            throw new IllegalArgumentException("Length must be non-negative.");

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        String randomString = sb.toString();

        generatedStrings.add(randomString);

        return randomString;
    }

    public static String generateUniqueRandomString(int length) {
        while (true) {
            String randomString = generateRandomString(length);
            if (Collections.frequency(generatedStrings, randomString) == 1)
                return randomString;
        }
    }

    private RandomStringGenerator() {
        // This class should not be instantiated.
    }
}
