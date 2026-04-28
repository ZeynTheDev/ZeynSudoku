module com.zeynthedev.zeynsudoku {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.zeynthedev.zeynsudoku to javafx.fxml;
    exports com.zeynthedev.zeynsudoku;
}
