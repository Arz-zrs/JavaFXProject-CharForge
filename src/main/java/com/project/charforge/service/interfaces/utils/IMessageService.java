package com.project.charforge.service.interfaces.utils;

public interface IMessageService {
    void warning(String title, String message);
    void error(String title, String message);
    void info(String title, String message);

    boolean confirm(String title, String headerMessage, String message);
    boolean confirm(String title, String message);
}
