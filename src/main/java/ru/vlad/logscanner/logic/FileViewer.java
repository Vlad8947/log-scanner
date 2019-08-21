package ru.vlad.logscanner.logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import ru.vlad.logscanner.AppVariables;
import ru.vlad.logscanner.controller.LogViewController;
import ru.vlad.logscanner.controller.MainViewController;
import ru.vlad.logscanner.model.LogFileModel;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileViewer implements Runnable {

    private LogViewController logViewController;

    public FileViewer(LogFileModel logFileModel, TabPane tabPane) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainViewController.class.getClassLoader().getResource(LogViewController.VIEW_PATH));
        Parent parent = (VBox) loader.load();
        logViewController = loader.getController();
        logViewController.setLogFileModel(logFileModel);
        Tab tab = new Tab(logFileModel.getFileName(), parent);
        tabPane.getTabs().add(tab);
    }
    
    @Override
    public void run() {
        readFile(logViewController);
        logViewController.previousMarkAction();
    }

    public static void readFile(LogViewController controller) {

        controller.getTextArea().clear();
        Charset charset = StandardCharsets.UTF_8;

        final int charsOnPage = AppVariables.CHARS_ON_PAGE;
        File file = controller.getLogFileModel().getFile();

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset)) {

            long page = controller.getLogFileModel().getCurrentPage();
            long skipChars = (page-1) * charsOnPage;
            if (skipChars != 0) {
                reader.skip(skipChars);
            }

            char[] buffer = new char[AppVariables.BUFFER_SIZE];
            int allRead = 0;

            for (int read = reader.read(buffer); read != -1; read = reader.read(buffer)) {

                allRead += read;
                if (allRead > charsOnPage) {
                    read -= (allRead - charsOnPage);
                    onView(read, buffer, controller);
                    return;
                }
                onView(read, buffer, controller);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void onView(int read, char[] buffer, LogViewController controller) {
        String text;

        if (read == buffer.length) {
            text = new String(buffer);
        } else {
            text = new String(Arrays.copyOf(buffer, read));
        }
        controller.getTextArea().appendText(text);
    }
    
    
}
