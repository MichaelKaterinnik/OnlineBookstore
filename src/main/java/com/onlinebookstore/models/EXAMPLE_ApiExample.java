package com.onlinebookstore.models;

import java.util.Objects;

/**
 * Клас-модель для опису об'єктів типу - EXAMPLE_ApiExample. Містить id, як ідентифікатор, а також інші поля,
 * які також відображені у таблицях БД.
 */
public class EXAMPLE_ApiExample {
    private Integer id;
    private Integer sectionId;

    private final String title;

    private final String codeSnippet;


    public EXAMPLE_ApiExample(Integer id, Integer sectionId, String title, String codeSnippet) {
        this.id = id;
        this.sectionId = sectionId;
        this.title = title;
        this.codeSnippet = codeSnippet;
    }

    public EXAMPLE_ApiExample(int sectionId, String title, String codeSnippet) {
        this.sectionId = sectionId;
        this.title = title;
        this.codeSnippet = codeSnippet;
    }


    public EXAMPLE_ApiExample(String title, String codeSnippet) {
        this.title = title;
        this.codeSnippet = codeSnippet;
    }

    public int getId() {
        return id;
    }

    public int getSectionId() {
        return sectionId;
    }

    public String getTitle() {
        return title;
    }

    public String getCodeSnippet() {
        return codeSnippet;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EXAMPLE_ApiExample that = (EXAMPLE_ApiExample) o;
        return id == that.id && sectionId == that.sectionId && Objects.equals(title, that.title) && Objects.equals(codeSnippet, that.codeSnippet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectionId, title, codeSnippet);
    }
}
