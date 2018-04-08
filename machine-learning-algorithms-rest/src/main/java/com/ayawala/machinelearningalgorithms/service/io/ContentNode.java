package com.ayawala.machinelearningalgorithms.service.io;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;

public class ContentNode {

    private final String name;

    private final String path;

    private final String basePath;

    private final long contentLength;

    private final ByteSource byteSource;

    public ContentNode(final String path, final long contentLength, final ByteSource byteSource) {
        this.path = path;
        this.contentLength = contentLength;
        this.byteSource = byteSource;
        this.name = "name";
        this.basePath = "base path";
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getBasePath() {
        return basePath;
    }

    public long getContentLength() {
        return contentLength;
    }

    public ByteSource getByteSource() {
        return byteSource;
    }

    public String getContentAsString() throws IOException {
        return byteSource.asCharSource(Charsets.UTF_8).read();
    }
}
