package ru.vlad.logscanner;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Dialog  {

    private Dialog() {
    }

    private static void dialog(Alert.AlertType type, String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void information(String title, String header, String content) {
        dialog(Alert.AlertType.INFORMATION, title, header, content);
    }

    public static void error(String title, String header, String content) {
        dialog(Alert.AlertType.ERROR, title, header, content);
    }

    public static void emptyFieldError(String field, String content) {
        String title = "Error";
        String header = "The field \"" + field + "\" is empty.";
        error(title, header, content);
    }

}
