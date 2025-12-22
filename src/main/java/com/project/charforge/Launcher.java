package com.project.charforge;

import com.sun.tools.javac.Main;

public class Launcher {
    public static void main(String[] args) {
        try {
            Main.main(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
