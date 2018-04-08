package com.ayawala.machinelearningalgorithms.model;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmMetadata {

    private String title;

    private List<AlgorithmAuthor> authors = new ArrayList<>();

    private String summary;

    private String publishDate;

    private String updateDate;

    private List<String> tags = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AlgorithmAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AlgorithmAuthor> authors) {
        this.authors = authors;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
