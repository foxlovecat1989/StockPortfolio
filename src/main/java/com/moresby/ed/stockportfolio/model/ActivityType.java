package com.moresby.ed.stockportfolio.model;

public enum ActivityType {
    COURSE("Course"),
    DISCUSSION("Discussion"),
    SPEECH("Speech");

    private String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
