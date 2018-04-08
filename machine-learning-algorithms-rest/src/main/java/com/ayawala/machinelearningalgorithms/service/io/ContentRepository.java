package com.ayawala.machinelearningalgorithms.service.io;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContentRepository {

    private final Map<String, ContentNode> source = new LinkedHashMap<>();

    public Map<String, ContentNode> getSource() {
        return source;
    }

    public ContentNode getContentNode(final String path) {
        return source.get(path);
    }

    public Collection<ContentNode> getContentNodes() {
        return source.values();
    }
}
