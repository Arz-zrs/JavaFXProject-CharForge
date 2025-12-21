package com.project.charforge.console;

//TODO: Add Abstraction for DIP
public final class Logs {
    public static void printError(String message){
        System.err.println(message);
    }

    public static void debugPrint(String message){
        System.out.println(message);
    }
}
