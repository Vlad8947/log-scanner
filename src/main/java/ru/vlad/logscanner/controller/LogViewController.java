package ru.vlad.logscanner.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.converter.NumberStringConverter;
import lombok.Getter;
import ru.vlad.logscanner.logic.OpenMark;
import ru.vlad.logscanner.logic.OpenPage;
import ru.vlad.logscanner.model.LogFileModel;

public class LogViewController {

    public static final String VIEW_PATH = "LogView.fxml";

    @Getter
    private LogFileModel logFileModel;

    @FXML @Getter
    private TextArea textArea;

    @FXML
    private Button prevMarkButton;
    @FXML
    private Button nextMarkButton;
    @FXML @Getter
    private TextField currentMarkTextField;
    private final LongProperty currentMark = new SimpleLongProperty();
    @FXML
    private Label markAmountLabel;

    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;
    @FXML @Getter
    private TextField currentPageTextField;
    private final IntegerProperty currentPage = new SimpleIntegerProperty();
    @FXML
    private Label pageAmountLabel;

    public LogViewController() {
    }

    @FXML
    private void initialize() {
        initCurrentMarkTextField();
        initCurrentPageTextField();

        prevMarkButton.setOnAction(event -> previousMarkAction());
        nextMarkButton.setOnAction(event -> nextMarkAction());
        prevPageButton.setOnAction(event -> OpenPage.previous(this));
        nextPageButton.setOnAction(event -> OpenPage.next(this));
    }

    public void setLogFileModel(LogFileModel logFileModel) {
        this.logFileModel = logFileModel;

        String currentMark = Integer.toString(logFileModel.getCurrentMarkIndex());
        String markAmount = Long.toString(logFileModel.getMarkCharList().size());
        currentMarkTextField.setText(currentMark);
        markAmountLabel.setText(markAmount);

        String currentPage = Long.toString(logFileModel.getCurrentPage());
        String pageAmount = Long.toString(logFileModel.getPageAmount());
        currentPageTextField.setText(currentPage);
        pageAmountLabel.setText(pageAmount);
    }

    private void initCurrentMarkTextField() {
        currentMarkTextField.textProperty().bindBidirectional(currentMark, new NumberStringConverter());
        currentMarkTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                OpenMark.open(this, logFileModel.getMark(currentMark.intValue()-1));
            }
        });
    }

    private void initCurrentPageTextField() {
        currentPageTextField.textProperty().bindBidirectional(currentPage, new NumberStringConverter());
        currentPageTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                OpenPage.open(this, currentPage.intValue());
            }
        });
    }

    public void previousMarkAction() {
        OpenMark.open(this, logFileModel.prevMark());
    }

    private void nextMarkAction() {
        OpenMark.open(this, logFileModel.nextMark());
    }

}
