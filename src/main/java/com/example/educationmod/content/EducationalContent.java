package com.example.educationmod.content;

/**
 * Represents a single piece of educational content.
 * Each content entry is defined by its topic, description, and optional additional metadata.
 */
public class EducationalContent {
    private String topic;    // Topic or category (e.g., Geology, History)
    private String content;  // The main educational content

    // Default constructor (required for Gson serialization/deserialization)
    public EducationalContent() {}

    // Constructor for manual initialization if needed
    public EducationalContent(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EducationalContent{" +
                "topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
