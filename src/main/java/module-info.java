module com.example.memoramamejora {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.memoramamejora to javafx.fxml;
    exports com.example.memoramamejora;
}