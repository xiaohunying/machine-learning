package com.ayawala.machinelearningalgorithms.model;

public class AlgorithmImage {

    private String path;

    private byte[] content;

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    public String getMimeType() {
        if(path != null) {
            String value = path.toLowerCase();
            if(value.endsWith(".png")){
                return "image/png";
            } else if(value.endsWith(".jpg")){
                return "image/jpeg";
            } else if(value.endsWith(".gif")){
                return "image/gif";
            } else if(value.endsWith(".svg")){
                return "image/svg+xml";
            }
        }
        return null;
    }
}
