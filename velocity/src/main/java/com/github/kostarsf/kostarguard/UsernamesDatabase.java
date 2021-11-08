package com.github.kostarsf.kostarguard;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class UsernamesDatabase {
    private static ArrayList<String> usernames = new ArrayList<>();
    private static Path usernamesListPath;

    public static void AddEntry(String username) {
        usernames.add(username);
        writeToFile();
    }

    public static String getEntry(String username) {
        if (usernames.size() > 0) {
            for (String entry: usernames) {
                if (entry.equalsIgnoreCase(username)) {
                    return entry;
                }
            }
        }

        return null;
    }

    public static boolean isEntryExist(String username) {
        String entry = getEntry(username);

        return entry != null && entry.equalsIgnoreCase(username);
    }

    public static void syncWithFile(Path usernamesList) {
        usernamesListPath = usernamesList;
        try {
            BufferedReader reader = Files.newBufferedReader(usernamesList);
            String line;
            do {
                line = reader.readLine();
                if (line != null && line.isBlank() == false) {
                    usernames.add(line);
                }
            } while (line != null);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile() {
        try {
            Files.write(usernamesListPath, usernames, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getEntriesCount() {
        return usernames.size();
    }
}
