package ru.vlad.logscanner.initializer;

import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.Getter;
import ru.vlad.logscanner.ViewLoader;
import ru.vlad.logscanner.controller.MainViewController;

import java.io.IOException;

public class AppInitializer {

    @Getter
    private Scene scene;

    public AppInitializer() throws IOException {
        init();
    }

    private void init() throws IOException {
        Parent mainView = ViewLoader.loadView(MainViewController.VIEW_PATH);
        scene = new Scene(mainView);
    }

}
