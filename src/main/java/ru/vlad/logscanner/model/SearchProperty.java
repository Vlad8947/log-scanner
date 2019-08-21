package ru.vlad.logscanner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchProperty {

    private String path;
    private String extension;
    private String searchText;

}
