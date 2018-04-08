package com.ayawala.machinelearningalgorithms.config;

import com.ayawala.machinelearningalgorithms.model.AlgorithmConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ConfigurationProperties(prefix = "machine-learning-algorithms")
public class ApplicationProperties {

    private Boolean isAsyncLoad = Boolean.TRUE;

    private String urlTemplate;

    private final Map<String, AlgorithmConfig> algorithms = new LinkedHashMap<>();

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public Map<String, AlgorithmConfig> getAlgorithms() {
        return algorithms;
    }

    public List<AlgorithmConfig> getAlgorithmConfigs() {
        return new ArrayList<>(algorithms.values());
    }

    public Boolean getAsyncLoad() {
        return isAsyncLoad;
    }

    public void setAsyncLoad(final Boolean asyncLoad) {
        isAsyncLoad = asyncLoad;
    }
}
