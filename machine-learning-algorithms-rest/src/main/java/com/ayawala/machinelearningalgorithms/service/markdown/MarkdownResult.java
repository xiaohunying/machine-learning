package com.ayawala.machinelearningalgorithms.service.markdown;

import java.util.ArrayList;
import java.util.List;

public class MarkdownResult {

    private String markdownContent;

    private String metadataContent;

    private String htmlContent;

    private final List<String> images = new ArrayList<>();

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public String getMetadataContent() {
        return metadataContent;
    }

    public void setMetadataContent(String metadataContent) {
        this.metadataContent = metadataContent;
    }

    public List<String> getImages() {
        return images;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
