package me.akraml.gamesbot.utility;

/**
 * Handles string-related functions.
 */
public class StringUtils {

    public static String addSymbolBetweenSpaces(final String toObfuscate) {
        return toObfuscate.replace(" ", " ' ");
    }

    public static String dismantle(String toDismantle) {
        final StringBuilder builder = new StringBuilder();
        toDismantle = toDismantle.replace(" ", "");
        boolean start = false;
        for (char c : toDismantle.toCharArray()) {
            if (!start) {
                start = true;
                builder.append(c);
            } else {
                builder.append(" ").append(c);
            }
        }
        return builder.toString();
    }

}
