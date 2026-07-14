package com.example.lnlbridgecalc;

public class JarLauncher {
    public static void main(String[] args) {
        // Вызываем метод main основного класса приложения
        // Это обходит проверку модулей JavaFX при старте JAR
        HelloApplication.main(args);
    }
}
