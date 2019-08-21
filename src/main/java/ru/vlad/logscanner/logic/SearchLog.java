package ru.vlad.logscanner.logic;

import ru.vlad.logscanner.AppVariables;
import ru.vlad.logscanner.controller.MainViewController;
import ru.vlad.logscanner.model.SearchProperty;

public class SearchLog {

    public static void find(SearchProperty searchProperty, MainViewController controller) {
        AppVariables.getExecutorService().execute(new LocalSearch(searchProperty, controller));
    }

}
