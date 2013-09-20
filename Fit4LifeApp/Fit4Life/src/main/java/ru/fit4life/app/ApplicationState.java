package ru.fit4life.app;

public class ApplicationState {

    private volatile static boolean flag;

    public static boolean isForegroud() {
        return flag;
    }

    public static void setForeground() {
        flag = true;
    }

    public static void setBackground() {
        flag = false;
    }
}