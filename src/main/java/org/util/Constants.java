package org.util;

public class Constants {

    public static String API_ROOT = "http://localhost:8081";

    public static boolean contains(String damageType) {
        for (DamageType c : DamageType.values()) {
            if (c.name().equals(damageType)) {
                return true;
            }
        }
        return false;
    }
}
