package com.ayawala.machinelearningalgorithms.service;

import com.ayawala.machinelearningalgorithms.config.ApplicationProperties;
import com.ayawala.machinelearningalgorithms.model.Algorithm;
import com.ayawala.machinelearningalgorithms.model.AlgorithmConfig;
import com.ayawala.machinelearningalgorithms.model.AlgorithmSummary;
import com.ayawala.machinelearningalgorithms.service.io.ContentRepository;
import com.ayawala.machinelearningalgorithms.service.io.ContentRepositoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class AlgorithmCatalog {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmCatalog.class);

    private Map<String, Algorithm> algorithms = Collections.synchronizedMap(new LinkedHashMap<>());

    private final AlgorithmParser algorithmParser;

    private final ApplicationProperties applicationProperties;

    private final ContentRepositoryFactory contentRepositoryFactory;

    public AlgorithmCatalog(final AlgorithmParser algorithmParser,
                            final ApplicationProperties applicationProperties,
                            final ContentRepositoryFactory contentRepositoryFactory) {
        this.algorithmParser = algorithmParser;
        this.applicationProperties = applicationProperties;
        this.contentRepositoryFactory = contentRepositoryFactory;
    }

    public Map<String, Algorithm> getAlgorithms() {
        return algorithms;
    }

    public Algorithm getAlgorithm(final String algorithmId) {
        return algorithms.get(algorithmId);
    }

    public List<AlgorithmSummary> getAlgorithmSummaries() {
        final List<AlgorithmSummary> algorithmSummaries = new ArrayList<>();
        algorithms.entrySet().stream().filter(entry -> entry.getValue().getType() == Algorithm.Type.PUBLISH)
                .forEach(entry -> algorithmSummaries.add(new AlgorithmSummary(entry.getValue())));
        return algorithmSummaries;
    }

    public List<AlgorithmSummary> getAlgorithmSummariesByTag(final String tag) {
        final List<AlgorithmSummary> algorithmSummaries = new ArrayList<>();
        algorithms.entrySet().stream().filter(entry -> entry.getValue().getType() == Algorithm.Type.PUBLISH)
                .forEach(entry -> {
                    final List<String> tags = entry.getValue().getMetadata().getTags();
                    algorithmSummaries.addAll(tags.stream().filter(thisTag -> thisTag.equalsIgnoreCase(tag))
                    .map(thisTag -> new AlgorithmSummary(entry.getValue())).collect(Collectors.toList()));
                });
        return algorithmSummaries;
    }


    @Async
    public Future<Void> loadAsync() {
        this.load();
        return new AsyncResult<>(null);
    }

    public void load() {
        LOGGER.info("Loading Algorithms [{}]", applicationProperties.getAlgorithmConfigs().size());
        this.algorithms = loadAlgorithms(applicationProperties.getAlgorithmConfigs());
    }

    private Map<String, Algorithm> loadAlgorithms(final List<AlgorithmConfig> algorithmConfigs) {
        final Map<String, Algorithm> algorithms = Collections.synchronizedMap(new LinkedHashMap<>());
        for(final AlgorithmConfig algorithmConfig : algorithmConfigs) {
            // Algorithm for publish
            if(algorithmConfig.getPublishVersion() != null) {
                Algorithm algorithm = new Algorithm(algorithmConfig.getId());
                algorithm.setType(Algorithm.Type.PUBLISH);
                if(!loadAlgorithm(algorithm, algorithmConfig)) {
                    algorithm = this.algorithms.get(algorithm.getId());
                }
                if(algorithm != null) {
                    algorithms.put(algorithm.getId(), algorithm);
                }
            }
            // Algorithm for develop
            if(algorithmConfig.getDevelopVersion() != null) {
                Algorithm algorithm = new Algorithm(algorithmConfig.getId() + ".develop");
                algorithm.setType(Algorithm.Type.DEVELOP);
                if(!loadAlgorithm(algorithm, algorithmConfig)) {
                    algorithm = this.algorithms.get(algorithm.getId());
                }
                if(algorithm != null) {
                    algorithms.put(algorithm.getId(), algorithm);
                }
            }
        }
        return algorithms;
    }

    private boolean loadAlgorithm(final Algorithm algorithm, final AlgorithmConfig algorithmConfig) {
        LOGGER.info("Loading algorithm [{}]", algorithm.toString());
        try {
            algorithm.setVersion(Algorithm.Type.DEVELOP == algorithm.getType() ?
                    algorithmConfig.getDevelopVersion() : algorithmConfig.getPublishVersion());
            final String path = getAlgorithmContent(applicationProperties.getUrlTemplate(),
                    algorithmConfig.getId(), algorithm.getVersion());
            final ContentRepository contentRepository = contentRepositoryFactory.build(path);
            return algorithmParser.parse(algorithm, contentRepository);
        } catch (final Throwable e) {
            LOGGER.warn("Unable to load [{}]: {}", algorithm.toString(), e.getMessage(), e);
            return false;
        }
    }

    private String getAlgorithmContent(final String urlTemplate, final String algorithmId, final String algorithmVersion) {
        return String.format(urlTemplate, algorithmId, algorithmVersion);
    }

}
