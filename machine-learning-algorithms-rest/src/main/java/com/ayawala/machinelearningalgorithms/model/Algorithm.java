package com.ayawala.machinelearningalgorithms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Algorithm {

    public enum Type{
        PUBLISH,
        DEVELOP
    }

    public Algorithm(final String id) {
        this.id = id;
    }

    private String id;

    private AlgorithmMetadata metadata = new AlgorithmMetadata();

    private String version;

    private String htmlContent;

    @JsonIgnore
    private final Map<String, AlgorithmImage> images = new ConcurrentHashMap<>();

    private Type type = Type.DEVELOP;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AlgorithmMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(AlgorithmMetadata metadata) {
        this.metadata = metadata;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Map<String, AlgorithmImage> getImages() {
        return images;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return id;
    }
}
