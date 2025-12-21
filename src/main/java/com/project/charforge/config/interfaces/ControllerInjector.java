package com.project.charforge.config.interfaces;

public interface ControllerInjector<T> {
    void inject(T controller);
}
