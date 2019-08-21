package ru.vlad.logscanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.vlad.logscanner.initializer.AppInitializer;

import java.io.IOException;

public class AppStarter extends Application {

    private String appTitle = "Log scanner";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppVariables.primaryStage = stage;
        AppInitializer initializer = new AppInitializer();
        Scene scene = initializer.getScene();
        stage.setScene(scene);
        stage.setTitle(appTitle);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        AppVariables.getExecutorService().shutdown();
    }

}
