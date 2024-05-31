package net.bobdb;

public class GPTResponse {
    String id;
    String object;
    Integer created;
    String model;
    Choice[] choices;
    Usage usage;
    String systemFingerprint;

    public Choice[] getChoices() {
        return choices;
    }
}