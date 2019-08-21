package ru.vlad.logscanner.logic;

import ru.vlad.logscanner.AppVariables;
import ru.vlad.logscanner.controller.LogViewController;
import ru.vlad.logscanner.model.LogFileModel;

public class OpenMark {

    public static void open(LogViewController controller, long charMark) {
        LogFileModel logFileModel = controller.getLogFileModel();
        long currentPage = logFileModel.getCurrentPage();
        int searchTextLength = logFileModel.getSearchTextLength();

        double tmpMarkPage = (double) charMark/AppVariables.CHARS_ON_PAGE;
        long markPage = (long) Math.ceil(tmpMarkPage);
        if (markPage != currentPage) {
            OpenPage.open(controller, markPage);
        }
        int from = (int) charMark % AppVariables.CHARS_ON_PAGE;
        int to = from + searchTextLength;
        controller.getTextArea().selectRange(from, to);
        String currentMark = Integer.toString(logFileModel.getCurrentMarkIndex()+1);
        controller.getCurrentMarkTextField().setText(currentMark);
    }

}
