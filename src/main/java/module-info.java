module org.example.project.turingmachineproject {
    requires javafx.fxml;
    requires MaterialFX;
    requires com.jfoenix;


    opens org.example.project.turingmachineproject to javafx.fxml;
    exports org.example.project.turingmachineproject;
}