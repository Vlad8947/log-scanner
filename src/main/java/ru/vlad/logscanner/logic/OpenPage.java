package ru.vlad.logscanner.logic;

import ru.vlad.logscanner.controller.LogViewController;

public class OpenPage {

    public static void previous(LogViewController controller) {
        long currentPage = controller.getLogFileModel().getCurrentPage();

        if (currentPage > 1) {
            currentPage--;
            setCurrentPageAndOpen(controller, currentPage);
        }
    }

    public static void next(LogViewController controller) {
        long currentPage = controller.getLogFileModel().getCurrentPage();
        long pageAmount = controller.getLogFileModel().getPageAmount();

        if (currentPage < pageAmount) {
            currentPage++;
            setCurrentPageAndOpen(controller, currentPage);
        }
    }

    public static void open(LogViewController controller, long page) {
        long pageAmount = controller.getLogFileModel().getPageAmount();

        if (page > pageAmount) {
            setCurrentPageAndOpen(controller, pageAmount);
            return;
        }
        if (page < 1) {
            setCurrentPageAndOpen(controller, 1);
            return;
        }
        setCurrentPageAndOpen(controller, page);
    }

    private static void setCurrentPageAndOpen(LogViewController controller, long currentPage) {
        controller.getLogFileModel().setCurrentPage(currentPage);
        controller.getCurrentPageTextField().setText(Long.toString(currentPage));
        FileViewer.readFile(controller);
    }

}
