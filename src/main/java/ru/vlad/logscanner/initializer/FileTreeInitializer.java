package ru.vlad.logscanner.initializer;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import ru.vlad.logscanner.logic.OpenFile;
import ru.vlad.logscanner.model.LogFileModel;

public class FileTreeInitializer {

    public static void init(TreeView<LogFileModel> treeView, TabPane tabPane) {
        ContextMenu contextMenu = contextMenu(treeView, tabPane);
        MultipleSelectionModel<TreeItem<LogFileModel>> selectionModel = treeView.getSelectionModel();
        EventHandler<ContextMenuEvent> contextMenuEventHandler = event -> {
            if (selectionModel.getSelectedItem() == null) {
                return;
            }
            contextMenu.show(treeView, event.getScreenX(), event.getScreenY());
        };
        treeView.setOnContextMenuRequested(contextMenuEventHandler);
        treeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 &&
                    selectionModel.getSelectedItem() != null &&
                    selectionModel.getSelectedItem().getValue().getMarkCharList() != null) {
                LogFileModel logFileModel = treeView.getSelectionModel().getSelectedItem().getValue();
                OpenFile.openFileInNewTab(logFileModel, tabPane);
            }
        });
    }

    private static ContextMenu contextMenu(TreeView treeView, TabPane tabPane) {
        ContextMenu contextMenu = new ContextMenu();
        String openFileINTLabel = "Open in new tab";
        MenuItem openFileINTMenuItem = new MenuItem(openFileINTLabel);
        MultipleSelectionModel<TreeItem<LogFileModel>> selectionModel = treeView.getSelectionModel();
        openFileINTMenuItem.setOnAction(event -> {
            OpenFile.openFileInNewTab(
                    selectionModel.getSelectedItem().getValue(), tabPane);
        });
        contextMenu.getItems().addAll(openFileINTMenuItem);
        contextMenu.setAutoHide(true);
        return contextMenu;
    }

}
