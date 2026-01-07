package com.skillbox.cryptobot.utils;

public class TextUtil {

    public static String formatDecimal(double number) {
        return String.format("%.2f", number);
    }

    public static String formatLargeNumber(long number) {
        return String.format("%,d", number);
    }

    public static String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
