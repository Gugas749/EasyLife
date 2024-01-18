package com.example.easylife.scripts;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class UniqueRandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 8;

    private Set<String> generatedStrings = new HashSet<>();
    private SecureRandom random = new SecureRandom();

    public String generateUniqueString() {
        String randomString;
        do {
            randomString = generateRandomString();
        } while (!generatedStrings.add(randomString)); // Ensure uniqueness

        return randomString;
    }

    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        UniqueRandomStringGenerator generator = new UniqueRandomStringGenerator();

        for (int i = 0; i < 10; i++) {
            String uniqueString = generator.generateUniqueString();
            System.out.println(uniqueString);
        }
    }
}
