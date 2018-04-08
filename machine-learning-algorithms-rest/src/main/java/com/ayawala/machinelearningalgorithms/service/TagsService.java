package com.ayawala.machinelearningalgorithms.service;

import com.ayawala.machinelearningalgorithms.model.AlgorithmSummary;
import com.ayawala.machinelearningalgorithms.model.TagSummary;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TagsService {

    private final AlgorithmCatalog algorithmCatalog;

    public TagsService(final AlgorithmCatalog algorithmCatalog) {
        this.algorithmCatalog = algorithmCatalog;
    }

    public List<TagSummary> getTagsPopulation() {
        final List<AlgorithmSummary> algorithmSummaries = algorithmCatalog.getAlgorithmSummaries();
        final Map<String, Long> tagStats = new HashMap<>();
        for(final AlgorithmSummary algorithmSummary : algorithmSummaries) {
            for(final String tag : algorithmSummary.getMetadata().getTags()) {
                if(tagStats.get(tag) != null) {
                    tagStats.put(tag, tagStats.get(tag) + 1);
                } else {
                    tagStats.put(tag, 1L);
                }
            }
        }

        final List<TagSummary> tagSummaries = tagStats.entrySet().stream()
                .map(tagStat -> new TagSummary(tagStat.getKey(), tagStat.getValue())).collect(Collectors.toList());

        tagSummaries.sort(Collections.reverseOrder(Comparator.comparing(TagSummary::getCount)));

        return tagSummaries;
    }
}
