package com.moresby.ed.stockportfolio.model;

public enum ActivityType {
    COURSE("Couse"),
    DISCUSSION("Discussion"),
    OUTDOOR("Outdoor-Activity");

    private String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
