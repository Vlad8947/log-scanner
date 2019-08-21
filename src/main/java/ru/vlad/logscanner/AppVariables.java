package ru.vlad.logscanner;

import javafx.stage.Stage;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AppVariables {

    //todo set 100 * 1000
    public static final int CHARS_ON_PAGE = 100 * 1000;
    public static final int BUFFER_SIZE = 8192;
    public static Stage primaryStage;

    @Getter
    private static ExecutorService executorService;

    static {
        initExecutorService();
    }

    private static void initExecutorService() {
        executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

}
