module com.zeynthedev.zeynsudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.base;
    requires java.desktop;

    opens com.zeynthedev.zeynsudoku to javafx.fxml;
    exports com.zeynthedev.zeynsudoku;
}
