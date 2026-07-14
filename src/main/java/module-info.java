module com.example.lnlbridgecalc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lnlbridgecalc to javafx.fxml;
    exports com.example.lnlbridgecalc;
}