package com.ayawala.machinelearningalgorithms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AlgorithmSummary {

    private Algorithm algorithm;

    public AlgorithmSummary(final Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getId() {
        return algorithm.getId();
    }

    public AlgorithmMetadata getMetadata() {
        return algorithm.getMetadata();
    }

    @JsonIgnore
    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
