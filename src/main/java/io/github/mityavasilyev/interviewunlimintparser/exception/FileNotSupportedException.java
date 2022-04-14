package io.github.mityavasilyev.interviewunlimintparser.exception;

/**
 * Indicates that currently program does not support certain file extension
 */
public class FileNotSupportedException extends RuntimeException {

    public FileNotSupportedException() {
    }

    public FileNotSupportedException(String message) {
        super(message);
    }
}
