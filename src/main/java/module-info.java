module com.example.javachess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.javachess to javafx.fxml;
    exports com.example.javachess;
}