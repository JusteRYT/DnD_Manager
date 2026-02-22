package com.example.dnd_manager;

public class Launcher {
    public static void main(String[] args) {
        checkLogLevel(args);
        MainApp.main(args);
    }

    private static void checkLogLevel(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--log=")) {
                String level = arg.substring(6).toUpperCase();
                System.setProperty("app.log.level", level);
            }
        }

        if (System.getProperty("app.log.level") == null) {
            System.setProperty("app.log.level", "INFO");
        }
    }
}

