package com.ayawala.machinelearningalgorithms.model;

public class AlgorithmConfig {

    private String id;

    private String developVersion;

    private String publishVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevelopVersion() {
        return developVersion;
    }

    public void setDevelopVersion(String developVersion) {
        this.developVersion = developVersion;
    }

    public String getPublishVersion() {
        return publishVersion;
    }

    public void setPublishVersion(String publishVersion) {
        this.publishVersion = publishVersion;
    }

    @Override
    public String toString() {
        return "Algorithm [" + id + "] version: {develop:" + developVersion + ", publish:" + publishVersion + "}";
    }
}
