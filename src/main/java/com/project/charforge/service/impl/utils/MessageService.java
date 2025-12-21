package com.project.charforge.service.impl.utils;

import com.project.charforge.service.interfaces.utils.IMessageService;
import com.project.charforge.ui.AlertUtils;

public class MessageService implements IMessageService {

    @Override
    public void warning(String title, String message) {
        AlertUtils.showWarning(title, message);
    }

    @Override
    public void error(String title, String message) {
        AlertUtils.showError(title, message);
    }

    @Override
    public void info(String title, String message) {
        AlertUtils.showInfo(title, message);
    }

    @Override
    public boolean confirm(String title, String headerMessage, String message) {
        return AlertUtils.showConfirmation(title, headerMessage, message);
    }

    @Override
    public boolean confirm(String title, String message) {
        return AlertUtils.showConfirmation(title, message);
    }
}
