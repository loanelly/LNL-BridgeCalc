package com.example.lnlbridgecalc;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelloController {
    @FXML
    private ComboBox<String> bridgeTypeBox;
    @FXML
    private TextField lengthField, densityField, strengthField;
    @FXML
    private VBox dynamicFieldsBox;
    @FXML
    private TextArea resultsArea;
    @FXML
    private Canvas drawingCanvas;

    private TextField fW, fH, fS1, fS2;

    @FXML
    public void initialize() {
        if (bridgeTypeBox != null) {
            bridgeTypeBox.getItems().addAll("Балочный мост", "Арочный мост", "Вантовый мост", "Висячий мост", "Рамный мост");
            bridgeTypeBox.getSelectionModel().selectFirst();
            onTypeChanged();
        }
    }

    @FXML
    void onTypeChanged() {
        dynamicFieldsBox.getChildren().clear();
        String type = bridgeTypeBox.getValue();
        fW = new TextField("3.5");
        fH = new TextField("1.8");
        dynamicFieldsBox.getChildren().addAll(new Label("Ширина полотна b (м):"), fW, new Label("Высота балки h (м):"), fH);

        if ("Балочный мост".equals(type)) {
            fS1 = new TextField("30.0");
            fS2 = new TextField("1.2");
            dynamicFieldsBox.getChildren().addAll(new Label("Нагрузка фур (кН/м):"), fS1, new Label("Коэф. k_dyn:"), fS2);
        } else if ("Арочный мост".equals(type)) {
            fS1 = new TextField("15.0");
            fS2 = new TextField("0.8");
            dynamicFieldsBox.getChildren().addAll(new Label("Подъем арки H (м):"), fS1, new Label("Толщина свода (м):"), fS2);
        } else if ("Вантовый мост".equals(type)) {
            fS1 = new TextField("45.0");
            fS2 = new TextField("24");
            dynamicFieldsBox.getChildren().addAll(new Label("Высота пилона (м):"), fS1, new Label("Кол-во вант (шт):"), fS2);
        } else if ("Висячий мост".equals(type)) {
            fS1 = new TextField("12.0");
            fS2 = new TextField("0.40");
            dynamicFieldsBox.getChildren().addAll(new Label("Провес кабеля f (м):"), fS1, new Label("Диаметр троса (м):"), fS2);
        } else if ("Рамный мост".equals(type)) {
            fS1 = new TextField("1.5");
            fS2 = new TextField("16.0");
            dynamicFieldsBox.getChildren().addAll(new Label("Толщина стоек (м):"), fS1, new Label("Высота стоек H (м):"), fS2);
        }
    }

    @FXML
    void onCalculate() {
        try {
            String type = bridgeTypeBox.getValue();
            double L = Double.parseDouble(lengthField.getText());
            double rho = Double.parseDouble(densityField.getText());
            double sig = Double.parseDouble(strengthField.getText());
            double b = Double.parseDouble(fW.getText());
            double h = Double.parseDouble(fH.getText());
            double s1 = Double.parseDouble(fS1.getText());
            double s2 = Double.parseDouble(fS2.getText());
            String out = "";

            switch (type) {
                case "Балочный мост": out = BridgesDatabase.calculateBeam(L, rho, sig, b, h, s1, s2); break;
                case "Арочный мост": out = BridgesDatabase.calculateArch(L, rho, sig, b, h, s1, s2); break;
                case "Вантовый мост": out = BridgesDatabase.calculateCable(L, rho, sig, b, h, s1, s2); break;
                case "Висячий мост": out = BridgesDatabase.calculateSuspension(L, rho, sig, b, h, s1, s2); break;
                case "Рамный мост": out = BridgesDatabase.calculateFrame(L, rho, sig, b, h, s1, s2); break;
            }

            // Выводим текст отчета на экран
            resultsArea.setText(out);

            // АВТОМАТИЧЕСКИЙ ЭКСПОРТ: Сохраняем этот же текст в файл на диске
            BridgesDatabase.exportReportToFile(type, out);

            // Перерисовываем векторную схему сил
            draw(type, L, h, s1, s2);
        } catch (NumberFormatException e) {
            resultsArea.setText("ОШИБКА: Проверьте формат чисел.");
        }
    }

    private void arr(GraphicsContext gc, double x1, double y1, double x2, double y2, String txt, Color c) {
        gc.setStroke(c);
        gc.setLineWidth(2.0);
        gc.strokeLine(x1, y1, x2, y2);
        double a = Math.atan2(y2 - y1, x2 - x1);
        gc.strokeLine(x2, y2, x2 - 8 * Math.cos(a - Math.PI / 6), y2 - 8 * Math.sin(a - Math.PI / 6));
        gc.strokeLine(x2, y2, x2 - 8 * Math.cos(a + Math.PI / 6), y2 - 8 * Math.sin(a + Math.PI / 6));
        gc.setFill(c);
        gc.setFont(new Font("Arial", 11));
        gc.fillText(txt, (x1 + x2) / 2 + 5, (y1 + y2) / 2 - 5);
    }

    private void draw(String type, double L, double hB, double s1, double s2) {
        if (drawingCanvas == null) return;
        GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
        double w = drawingCanvas.getWidth(), h = drawingCanvas.getHeight();
        gc.clearRect(0, 0, w, h);

        gc.setFill(Color.web("#78909C"));
        gc.fillRect(60, h - 110, 40, 110);
        gc.fillRect(w - 100, h - 110, 40, 110);

        double visualBeamH = Math.min(hB * 8, 25);

        switch (type) {
            case "Балочный мост":
                gc.fillRect(w / 2 - 15, h - 110, 30, 110);
                gc.setFill(Color.web("#22A7F0"));
                gc.fillRect(50, h - 110 - visualBeamH, w - 100, visualBeamH);
                arr(gc, w / 2, h - 160, w / 2, h - 115, "P_live + G_self", Color.RED);
                arr(gc, 80, h, 80, h - 105, "R1", Color.GREEN);
                arr(gc, w - 80, h, w - 80, h - 105, "R2", Color.GREEN);
                break;
            case "Арочный мост":
                double arH = Math.min(s1 * 4, 90);
                gc.setStroke(Color.web("#546E7A"));
                gc.setLineWidth(Math.min(s2 * 10, 12));
                gc.strokeArc(60, h - 110 - arH / 2, w - 120, arH, 0, 180, javafx.scene.shape.ArcType.OPEN);
                gc.setFill(Color.web("#22A7F0"));
                gc.fillRect(50, h - 110 - visualBeamH, w - 100, visualBeamH);
                arr(gc, w / 2, h - 150, w / 2, h - 115, "G", Color.RED);
                arr(gc, 55, h - 110, 15, h - 110, "H_thrust", Color.DARKORANGE);
                arr(gc, w - 55, h - 110, w - 15, h - 110, "H_thrust", Color.DARKORANGE);
                break;
            case "Вантовый мост":
                double pyH = Math.min(s1 * 3.2, 140);
                gc.setFill(Color.web("#455A64"));
                gc.fillRect(w / 2 - 10, h - 110 - pyH, 20, pyH + 110);
                gc.setFill(Color.web("#22A7F0"));
                gc.fillRect(50, h - 110 - visualBeamH, w - 100, visualBeamH);
                gc.setStroke(Color.web("#FFB300"));
                gc.setLineWidth(1.5);
                gc.strokeLine(w / 2, h - 110 - pyH + 20, 120, h - 110);
                gc.strokeLine(w / 2, h - 110 - pyH + 20, w - 120, h - 110);
                arr(gc, 120, h - 110, (w / 2 + 120) / 2, (2 * h - 110 - pyH + 20) / 2, "T_max", Color.DARKORANGE);
                arr(gc, w / 2, h - 110 - pyH, w / 2, h - 110, "P_pylon", Color.RED);
                break;
            case "Висячий мост":
                gc.setFill(Color.web("#37474F"));
                gc.fillRect(95, h - 200, 15, 200);
                gc.fillRect(w - 110, h - 200, 15, 200);
                gc.setFill(Color.web("#22A7F0"));
                gc.fillRect(50, h - 110 - visualBeamH, w - 100, visualBeamH);
                gc.setStroke(Color.web("#D32F2F"));
                gc.setLineWidth(3.0);
                double caF = Math.min(s1 * 7, 85);
                gc.strokeArc(100, h - 200, w - 210, caF * 2, 180, 180, javafx.scene.shape.ArcType.OPEN);
                arr(gc, 102, h - 200, 140, h - 160, "T_support", Color.DARKORANGE);
                arr(gc, 60, h - 110, 15, h - 110, "H_anchor", Color.RED);
                break;
            case "Рамный мост":
                double thick = Math.min(s1 * 8, 15);
                gc.setStroke(Color.web("#37474F"));
                gc.setLineWidth(thick);

                // Внутренние края берегов жестко привязаны к координатам 100 и w - 100
                double leftBankX = 100.0;
                double rightBankX = w - 100.0;

                // Стойки рамы сходятся к центру под небольшим углом для устойчивости (инженерный подкос)
                double leftTopX = leftBankX + 40.0;
                double rightTopX = rightBankX - 40.0;

                // Отрисовка ног рамы (от края берега до нижней грани полотна)
                gc.strokeLine(leftBankX, h - 110, leftTopX, h - 110 - visualBeamH);
                gc.strokeLine(rightBankX, h - 110, rightTopX, h - 110 - visualBeamH);

                // Дорожное полотно поверх стоек
                gc.setFill(Color.web("#22A7F0"));
                gc.fillRect(50, h - 110 - visualBeamH, w - 100, visualBeamH);

                // Локальная привязка стрелок сил к левому узлу сопряжения рамы
                arr(gc, leftTopX, h - 110 - visualBeamH, leftTopX - 15, h - 110 - visualBeamH + 50, "N_col", Color.RED);
                gc.setStroke(Color.PURPLE);
                gc.strokeOval(leftTopX - 10, h - 110 - visualBeamH - 10, 20, 20);
                gc.fillText("M_joint", leftTopX + 15, h - 110 - visualBeamH);
                break;
        }
        gc.setStroke(Color.web("#E53935"));
        gc.setLineWidth(1.0);
        gc.strokeLine(60, h - 20, w - 60, h - 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Расчетный пролет L = " + L + " м", w / 2 - 100, h - 5);
    }
}
