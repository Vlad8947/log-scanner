package ru.vlad.logscanner.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import lombok.Setter;
import ru.vlad.logscanner.AppVariables;
import ru.vlad.logscanner.Dialog;
import ru.vlad.logscanner.initializer.FileTabPaneInitializer;
import ru.vlad.logscanner.initializer.FileTreeInitializer;
import ru.vlad.logscanner.logic.SearchLog;
import ru.vlad.logscanner.model.LogFileModel;
import ru.vlad.logscanner.model.SearchProperty;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

@Getter
public class MainViewController {

    public static final String VIEW_PATH = "MainView.fxml";

    @Setter
    private Closeable searchThread;

    @FXML
    private Label pathLabel;
    @FXML
    private Label extensionLabel;
    @FXML
    private Label searchTextLabel;

    @FXML
    private VBox searchBox;
    @FXML
    private TextField pathTextField;
    @FXML
    private Button selectDirectoryButton;
    @FXML
    private TextField fileExtTextField;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;

    @FXML
    private VBox searchInProgressBox;
    @FXML
    private Button stopSearchButton;

    @FXML
    private HBox foundBox;
    @FXML
    private Label foundFilesNumberLabel;

    @FXML
    private SplitPane splitPane;
    @FXML
    private volatile TreeView<LogFileModel> treeView;
    @FXML
    private TabPane tabPane;

    public MainViewController() {
    }

    @FXML
    private void initialize() {
        foundBox.setVisible(false);
        initSelectDirButton();
        initSearchButton();
        initSearchInProgressVBox();
        FileTreeInitializer.init(treeView, tabPane);
        FileTabPaneInitializer.init(tabPane);
    }

    private void initSearchInProgressVBox() {
        setUnsearchFaze();
        stopSearchButton.setOnAction(event -> {
            try {
                searchThread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initSelectDirButton() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        selectDirectoryButton.setOnAction(event -> {
            File dir = directoryChooser.showDialog(AppVariables.primaryStage);
            if (dir != null) {
                pathTextField.setText(dir.getAbsolutePath());
            } else {
                pathTextField.setText(null);
            }
        });
    }

    private void initSearchButton() {
        searchButton.setOnAction(even -> {
            if (emptyFieldExist()) {
                return;
            }
            setSearchFaze();
            SearchProperty searchProperty = new SearchProperty(
                    pathTextField.getText(),
                    "." + fileExtTextField.getText(),
                    searchTextField.getText());
            SearchLog.find(searchProperty, this);
        });
    }

    private boolean emptyFieldExist() {
        if (pathTextField.getText() == null || pathTextField.getText().isEmpty()) {
            ru.vlad.logscanner.Dialog.emptyFieldError(pathLabel.getText(), "");
            return true;
        }
        if (fileExtTextField.getText().isEmpty()) {
            ru.vlad.logscanner.Dialog.emptyFieldError(extensionLabel.getText(), "");
            return true;
        }
        if (searchTextField.getText().isEmpty()) {
            Dialog.emptyFieldError(searchTextLabel.getText(), "");
            return true;
        }
        return false;
    }

    public void setSearchFaze() {
        searchBox.setDisable(true);
        searchInProgressBox.setVisible(true);
        foundBox.setVisible(false);
    }

    public void setUnsearchFaze() {
        searchBox.setDisable(false);
        searchInProgressBox.setVisible(false);
    }

    public void setFoundFiles(int number) {
        String foundText = Integer.toString(number);
        Platform.runLater(() -> foundFilesNumberLabel.setText(foundText));
        foundBox.setVisible(true);
    }

}
