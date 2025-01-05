package org.example.model;

public enum PresenceType {
    O,
    N,
    S,
    U;

    public static Integer stringToPresenceType(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        switch (string.toUpperCase()) {
            case "O":
                return 1;
            case "N":
                return 2;
            case "S":
                return 3;
            case "U":
                return 4;
            default:
                return null;
        }
    }
}
