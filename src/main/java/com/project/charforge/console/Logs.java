package com.project.charforge.console;

public final class Logs {
    public static void printError(String message){
        System.err.println(message);
    }

    public static void debugPrint(String message){
        System.out.println(message);
    }
}
