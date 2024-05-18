package org.util;

public class Debug {

    public static boolean debug = false;
    public static int delay = 30000;

    public static void delay() {
        if (Debug.debug && Debug.delay > 0) {
            try {
                Thread.sleep(Debug.delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
