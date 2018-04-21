package me.xofu.simplechunk.utils;

public class BooleanUtils {

    public static boolean isBoolean(String string) {
        return "true".equalsIgnoreCase(string) || "false".equalsIgnoreCase(string);
    }

    public static boolean isFalse(boolean bool) {
        return bool == false;
    }
}
