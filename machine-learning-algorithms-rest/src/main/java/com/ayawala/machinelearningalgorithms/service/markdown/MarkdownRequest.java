package com.ayawala.machinelearningalgorithms.service.markdown;

public class MarkdownRequest {

    private String markdown;

    private String imagePrefix;

    private String linkPrefix;

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getImagePrefix() {
        return imagePrefix;
    }

    public void setImagePrefix(String imagePrefix) {
        this.imagePrefix = imagePrefix;
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }

    public void setLinkPrefix(String linkPrefix) {
        this.linkPrefix = linkPrefix;
    }
}
