package com.ayawala.machinelearningalgorithms.service.markdown;

public class MarkdownException extends RuntimeException{

    public MarkdownException(final String message){
        super(message);
    }

    public MarkdownException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
