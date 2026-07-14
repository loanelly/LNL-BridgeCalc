package com.example.lnlbridgecalc;

import java.util.ArrayList;
import java.util.List;

public class BridgesDatabase {
    private static final List<String> history = new ArrayList<>();
    private static final double E = 2.0e11;
    private static final double G = 9.80665;

    private static String check(double k) {
        if (k >= 1.5) return String.format("БЕЗОПАСНО (Запас: %.2f >= 1.5)", k);
        if (k >= 1.0) return String.format("КРИТИЧЕСКИЙ УРОВЕНЬ! Запас: %.2f. Опасно!", k);
        return String.format("РАЗРУШЕНИЕ! Запас %.2f < 1.0. МОСТ РУХНЕТ!", k);
    }

    public static String calculateBeam(double L, double rho, double sigB, double b, double h, double load, double kD) {
        double area = b * h;
        double qS = area * rho * G;
        double qL = load * 1000.0 * kD;
        double qT = qS + qL;
        double M = (qT * Math.pow(L, 2)) / 8.0;
        double W = (b * Math.pow(h, 2)) / 6.0;
        double sAct = M / W;
        double kS = sigB / sAct;
        double def = (5.0 * qT * Math.pow(L, 4)) / (384.0 * E * ((b * Math.pow(h, 3)) / 12.0)) * 1000.0;

        return String.format(
                "=== РАСЧЕТ БАЛОЧНОГО МОСТА ===\n" +
                        "ГАБАРИТЫ: L = %.1f м | b = %.2f м | h = %.2f м\n" +
                        "МЕТРИКИ:\n" +
                        "  Погонный вес: %.2f кН/м | Момент M_max: %.2f кН·м\n" +
                        "  Напряжение σ_act: %.2f МПа (Предел: %.2f МПа)\n" +
                        "  Прогиб f_max: %.2f мм\n" +
                        "АНАЛИЗ НАДЕЖНОСТИ LNL-ENGINEERING:\n" +
                        "  Масса пролета: %.1f т | Запас прочности: %.2f\n" +
                        "  СТАТУС: %s\n" +
                        "=========================================",
                L, b, h, qS/1000.0, M/1000.0, sAct/1e6, sigB/1e6, def, (area * L * rho)/1000.0, kS, check(kS)
        );
    }

    public static String calculateArch(double L, double rho, double sigB, double b, double h, double H, double t) {
        double areaA = t * b;
        double wT = (b * h * L * rho * G) + (areaA * L * 1.25 * rho * G);
        double ang = Math.atan(H / (L / 2.0));
        double thrust = (wT * L) / (8.0 * H);
        double N = thrust / Math.cos(ang);
        double sComp = N / areaA;
        double kS = sigB / sComp;

        return String.format(
                "=== РАСЧЕТ АРОЧНОГО МОСТА ===\n" +
                        "ГАБАРИТЫ: L = %.1f м | b = %.2f м | h = %.2f м\n" +
                        "  Подъем H: %.1f м | Толщина арки t: %.2f м\n" +
                        "МЕТРИКИ:\n" +
                        "  Угол оси α: %.1f° | Распор H_thrust: %.2f кН\n" +
                        "  Сила сжатия N_max: %.2f кН | Напряжение σ_comp: %.2f МПа\n" +
                        "АНАЛИЗ НАДЕЖНОСТИ LNL-ENGINEERING:\n" +
                        "  Полная масса: %.1f т | Запас на сжатие: %.2f\n" +
                        "  СТАТУС: %s\n" +
                        "=========================================",
                L, b, h, H, t, Math.toDegrees(ang), thrust/1000.0, N/1000.0, sComp/1e6, wT/G/1000.0, kS, check(kS)
        );
    }

    public static String calculateCable(double L, double rho, double sigB, double b, double h, double H, double count) {
        double wD = b * h * L * rho * G;
        double areaC = 0.0012;
        double ang = Math.atan((L / 2.0) / H);
        double T = (wD / 2.0) / (Math.cos(ang) * count);
        double sCab = T / areaC;
        double kS = sigB / sCab;

        return String.format(
                "=== РАСЧЕТ ВАНТОВОГО МОСТА ===\n" +
                        "ГАБАРИТЫ: L = %.1f м | b = %.2f м | h = %.2f м\n" +
                        "  Пилон H: %.1f м | Ванты: %.0f шт\n" +
                        "МЕТРИКИ:\n" +
                        "  Угол β: %.1f° | Натяжение ванты T_max: %.2f кН\n" +
                        "  Напряжение σ_cable: %.2f МПа | Нагрузка на пилон: %.2f кН\n" +
                        "АНАЛИЗ НАДЕЖНОСТИ LNL-ENGINEERING:\n" +
                        "  Вес полотна: %.1f т | Запас вантовой системы: %.2f\n" +
                        "  СТАТУС: %s\n" +
                        "=========================================",
                L, b, h, H, count, Math.toDegrees(ang), T/1000.0, sCab/1e6, wD/1000.0, wD/G/1000.0, kS, check(kS)
        );
    }

    public static String calculateSuspension(double L, double rho, double sigB, double b, double h, double f, double d) {
        double areaC = Math.PI * Math.pow(d / 2.0, 2);
        double wD = b * h * L * rho * G;
        double thrust = ((wD / L) * Math.pow(L, 2)) / (8.0 * f);
        double T = thrust * Math.sqrt(1.0 + 16.0 * Math.pow(f / L, 2));
        double sAct = T / areaC;
        double kS = sigB / areaC;

        return String.format(
                "=== РАСЧЕТ ВИСЯЧЕГО МОСТА ===\n" +
                        "ГАБАРИТЫ: L = %.1f м | b = %.2f м | h = %.2f м\n" +
                        "  Провес f: %.1f м | Диаметр кабеля d: %.3f м\n" +
                        "МЕТРИКИ:\n" +
                        "  Сечение кабеля A: %.4f м² | Распор H_thrust: %.2f кН\n" +
                        "  Натяжение T_support: %.2f кН | Напряжение σ_act: %.2f МПа\n" +
                        "АНАЛИЗ НАДЕЖНОСТИ LNL-ENGINEERING:\n" +
                        "  Масса структуры: %.1f т | Запас главного кабеля: %.2f\n" +
                        "  СТАТУС: %s\n" +
                        "=========================================",
                L, b, h, f, d, areaC, thrust/1000.0, T/1000.0, sAct/1e6, wD/G/1000.0, kS, check(kS)
        );
    }

    public static String calculateFrame(double L, double rho, double sigB, double b, double hB, double t, double H) {
        double wN = ((b * hB * L) + (b * t * H * 2)) * rho * G;
        double M = ((wN / L) * Math.pow(L, 2)) / 12.0;
        double sComb = M / ((b * Math.pow(hB, 2)) / 6.0);
        double kS = sigB / sComb;

        return String.format(
                "=== РАСЧЕТ РАМНОГО МОСТА ===\n" +
                        "ГАБАРИТЫ: L = %.1f м | b = %.2f м | h_балки = %.2f м\n" +
                        "  Высота рамы H: %.1f м | Толщина стоек t: %.2f м\n" +
                        "МЕТРИКИ:\n" +
                        "  Жесткий момент M_support: %.2f кН·м | Напряжение σ_comb: %.2f МПа\n" +
                        "АНАЛИЗ НАДЕЖНОСТИ LNL-ENGINEERING:\n" +
                        "  Вес каркаса рамы: %.1f т | Запас рамных узлов: %.2f\n" +
                        "  СТАТУС: %s\n" +
                        "=========================================",
                L, b, hB, H, t, M/1000.0, sComb/1e6, wN/G/1000.0, kS, check(kS)
        );
    }

    public static List<String> getHistory() { return history; }
    public static void clearDatabase() { history.clear(); }
    public static void exportReportToFile(String type, String content) {
        String baseName = "LNL_Report_" + type.replaceAll("\\s+", "_") + ".txt";
        java.io.File targetFile;

        try {
            // Определяем физическое местоположение запущенного класса/JAR-файла
            java.io.File jarPath = new java.io.File(BridgesDatabase.class
                    .getProtectionDomain().getCodeSource().getLocation().toURI());

            // Получаем родительскую папку (где лежит этот JAR)
            java.io.File currentFolder = jarPath.getParentFile();

            // Формируем абсолютный путь прямо в этой папке
            targetFile = new java.io.File(currentFolder, baseName);
        } catch (Exception e) {
            // Резервный вариант, если URI не определился (запуск из кастомной среды)
            targetFile = new java.io.File(baseName);
        }

        try (java.io.FileWriter writer = new java.io.FileWriter(targetFile, false)) {
            writer.write(content);
            System.out.println("Отчет сохранен по пути: " + targetFile.getAbsolutePath());
        } catch (java.io.IOException e) {
            System.err.println("Ошибка записи отчета на диск: " + e.getMessage());
        }
    }

}
