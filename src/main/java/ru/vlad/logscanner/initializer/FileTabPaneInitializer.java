package ru.vlad.logscanner.initializer;

import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;

public class FileTabPaneInitializer {

    public static void init(TabPane tabPane) {
        ContextMenu contextMenu = contextMenu(tabPane);
        EventHandler<ContextMenuEvent> contextMenuEventHandler = event -> {
            if (tabPane.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            contextMenu.show(tabPane, event.getScreenX(), event.getScreenY());
        };
        tabPane.setOnContextMenuRequested(contextMenuEventHandler);
    }

    private static ContextMenu contextMenu(TabPane tabPane) {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        ContextMenu contextMenu = new ContextMenu();
        String closeLabel = "Close";
        MenuItem closeTabMenuItem = new MenuItem(closeLabel);
        closeTabMenuItem.setOnAction(event -> {
            TabPaneBehavior tabPaneBehavior = ((TabPaneSkin) tabPane.getSkin()).getBehavior();
            Tab selectedTab = selectionModel.getSelectedItem();
            tabPaneBehavior.closeTab(selectedTab);
        });
        contextMenu.getItems().addAll(closeTabMenuItem);
        contextMenu.setAutoHide(true);
        return contextMenu;
    }

}
