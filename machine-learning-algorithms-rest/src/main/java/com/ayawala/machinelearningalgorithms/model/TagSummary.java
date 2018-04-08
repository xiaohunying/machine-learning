package com.ayawala.machinelearningalgorithms.model;

public class TagSummary {

    private String name;

    private Long count;

    public TagSummary(final String name, final Long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(final Long count) {
        this.count = count;
    }
}
