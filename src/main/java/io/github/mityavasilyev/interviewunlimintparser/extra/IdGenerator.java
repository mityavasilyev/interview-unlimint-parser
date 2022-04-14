package io.github.mityavasilyev.interviewunlimintparser.extra;

/**
 * Used to generate IDs for converted order entries
 */
public interface IdGenerator {

    /**
     * Returns new ID for further usage
     * @return new ID
     */
    Long getNewId();
}
