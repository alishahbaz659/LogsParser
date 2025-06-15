package com.example.LogsParser;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LogsParserApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                LogsParserApplication.class.getResource("/fxml/main-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

        loadStylesheet(scene);

        primaryStage.setTitle("Logs Parser - Desktop Application");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void loadStylesheet(Scene scene) {
        try {
            var cssResource = getClass().getResource("/css/application.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
            }
        } catch (Exception e) {
            System.out.println("Note: CSS styling not loaded - " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
