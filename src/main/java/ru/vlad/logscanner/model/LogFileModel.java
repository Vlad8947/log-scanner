package ru.vlad.logscanner.model;

import lombok.Getter;
import lombok.Setter;
import ru.vlad.logscanner.AppVariables;

import java.io.File;
import java.util.List;

@Getter
public class LogFileModel {

    private File file;
    private String fileName;
    private long charAmount;
    private long pageAmount;
    private List<Long> markCharList;
    private SearchProperty searchProperty;
    private int searchTextLength;
    @Setter
    private long currentPage = 1;
    @Setter
    private int currentMarkIndex = 1;

    public LogFileModel(String fileName) {
        this.fileName = fileName;
    }

    public LogFileModel(File file) {
        this.file = file;
        fileName = file.getName();
    }

    public LogFileModel(File file, List<Long> markCharList, SearchProperty searchProperty, long charAmount) {
        this(file);
        this.markCharList = markCharList;
        this.searchProperty = searchProperty;
        this.charAmount = charAmount;
        searchTextLength = searchProperty.getSearchText().length();
        float tempPageAmount = (float) charAmount/AppVariables.CHARS_ON_PAGE;
        pageAmount = (int) Math.ceil(tempPageAmount);
    }

    @Override
    public String toString() {
        return fileName;
    }

    public long prevMark() {
        if (currentMarkIndex > 0) currentMarkIndex--;
        return markCharList.get(currentMarkIndex);
    }

    public long nextMark() {
        if (currentMarkIndex < markCharList.size()-1) currentMarkIndex++;
        return markCharList.get(currentMarkIndex);
    }

    public long getMark(int markIndex) {
        if (markIndex < 0) {
            currentMarkIndex = 1;
        }
        else if (markIndex >= markCharList.size()) {
            currentMarkIndex = markCharList.size()-1;
        }
        else {
            currentMarkIndex = markIndex;
        }
        return markCharList.get(currentMarkIndex);
    }
}
