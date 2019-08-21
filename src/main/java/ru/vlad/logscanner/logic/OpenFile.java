package ru.vlad.logscanner.logic;

import javafx.scene.control.TabPane;
import ru.vlad.logscanner.AppVariables;
import ru.vlad.logscanner.model.LogFileModel;

import java.io.IOException;

public class OpenFile {

    public static void openFileInNewTab(LogFileModel logFileModel, TabPane tabPane) {
        try {
            AppVariables.getExecutorService().execute(new FileViewer(logFileModel, tabPane));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
