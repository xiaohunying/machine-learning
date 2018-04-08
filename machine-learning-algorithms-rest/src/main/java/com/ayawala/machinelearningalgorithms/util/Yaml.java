package com.ayawala.machinelearningalgorithms.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;

public class Yaml {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static <T> T readValue(String value, Class<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(value, type);
    }
}
