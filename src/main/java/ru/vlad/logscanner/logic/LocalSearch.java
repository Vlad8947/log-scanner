package ru.vlad.logscanner.logic;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import ru.vlad.logscanner.AppVariables;
import ru.vlad.logscanner.controller.MainViewController;
import ru.vlad.logscanner.model.LogFileModel;
import ru.vlad.logscanner.model.SearchProperty;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LocalSearch implements Runnable, Closeable {

    private boolean isShutDown = false;
    private SearchProperty searchProperty;
    private MainViewController controller;
    private TreeView<LogFileModel> treeView;
    private TreeItem<LogFileModel> rootTreeItem;
    private List<File> logFileList = new ArrayList<>();
    private List<LogFileModel> logFileModelList = new ArrayList<>();

    public LocalSearch(SearchProperty searchProperty, MainViewController controller) {
        this.searchProperty = searchProperty;
        this.controller = controller;
        this.treeView = controller.getTreeView();
        controller.setSearchThread(this);
    }

    @Override
    public void run() {
        File file = new File(searchProperty.getPath());
        rootTreeItem = new TreeItem<>(new LogFileModel(file));
        Platform.runLater(() -> {
            treeView.setRoot(rootTreeItem);
        });
        searchLogFiles(file);
        searchText();
        if(isShutDown) {
            return;
        }
        controller.setUnsearchFaze();
        controller.setFoundFiles(logFileModelList.size());
    }

    private void searchLogFiles(File dir) {
        if (isShutDown) {
            return;
        }
        for (File file: dir.listFiles()) {
            if (file.isDirectory()) {
                searchLogFiles(file);
            }
            else if (file.getName().endsWith(searchProperty.getExtension())) {
                logFileList.add(file);
            }
        }
    }

    private void searchText() {
        if (isShutDown) {
            return;
        }
        char[] searchChars = searchProperty.getSearchText().toCharArray();
        final int BUFFER_SIZE = AppVariables.BUFFER_SIZE;
        char[] buffer = new char[BUFFER_SIZE];

        Charset charset = StandardCharsets.UTF_8;

        for (File file: logFileList) {
            List<Long> markCharList = new ArrayList<>();
            long charAmount = 0;

            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), charset)) {
                int read;
                int rightChars = 0;
                int counter;

                while ((read = reader.read(buffer)) != -1) {
                    for (counter = 0; counter < read; ) {
                        if (buffer[counter] == searchChars[rightChars]) {
                            rightChars++;
                            if (rightChars == searchChars.length) {
                                markCharList.add(charAmount + counter+1 - rightChars);
                                rightChars = 0;
                            }
                        }
                        else if (rightChars > 0) {
                            rightChars = 0;
                            continue;
                        }
                        counter++;
                    }
                    if (isShutDown) {
                        return;
                    }
                    charAmount += read;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (markCharList.isEmpty()) {
                continue;
            }
            LogFileModel logFileModel = new LogFileModel(file, markCharList, searchProperty, charAmount);
            logFileModelList.add(logFileModel);

            TreeItem<LogFileModel> parentItem = getParentItem(file);
            parentItem.getChildren().add(new TreeItem<>(logFileModel));
        }
    }

    private TreeItem<LogFileModel> getParentItem(File file) {

        final String slash = "\\\\";
        String[] filePathArr = file.getAbsolutePath().split(slash);
        String[] rootFilePathArr = rootTreeItem.getValue().getFile().getAbsolutePath().split(slash);

        TreeItem<LogFileModel> parentItem = rootTreeItem;

        for (int i = rootFilePathArr.length; i < (filePathArr.length - 1); i++) {

            boolean parentChanged = false;
            for (TreeItem<LogFileModel> item : parentItem.getChildren()) {
                if (item.getValue().getFileName().equals(filePathArr[i])) {
                    parentItem = item;
                    parentChanged = true;
                    break;
                }
            }
            if (parentChanged) {
                continue;
            }
            TreeItem<LogFileModel> item = new TreeItem<>(new LogFileModel(filePathArr[i]));
            parentItem.getChildren().add(item);
            parentItem = item;
        }
        return parentItem;
    }

    @Override
    public void close() throws IOException {
        isShutDown = true;
        controller.setUnsearchFaze();
        controller.getFoundBox().setVisible(false);
        Platform.runLater(() -> treeView.getRoot().getChildren().clear());
    }
}
