module chatClient {
    requires javafx.controls;
    requires javafx.fxml;


    opens chatClient to javafx.fxml;
    exports chatClient;
    exports chatClient.controllers;
    opens chatClient.controllers to javafx.fxml;
}
