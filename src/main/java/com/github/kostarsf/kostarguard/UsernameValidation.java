package com.github.kostarsf.kostarguard;

import java.util.ArrayList;

public class UsernameValidation {
    private static ArrayList<String> usernames;

    private boolean newEntry;
    private boolean isPassed;
    private String validUsername;
    private String requestedUsername;

    public UsernameValidation(String username) {
        requestedUsername = username;

        if (UsernamesDatabase.isEntryExist(username)) {
            newEntry = false;
            validUsername = UsernamesDatabase.getEntry(username);
            isPassed = username.equals(validUsername);
        } else {
            UsernamesDatabase.AddEntry(username);
            newEntry = true;
            isPassed = true;
            validUsername = username;
        }
    }

    public boolean isNewEntry() {
        return newEntry;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public String getValidUsername() {
        return validUsername;
    }

    public  String getRequestedUsername() {
        return requestedUsername;
    }
}