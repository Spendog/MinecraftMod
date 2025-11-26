package com.example.educationmod;

import java.util.List;

/**
 * TopicMetadata - Metadata for organizing topics with tags and sections
 */
public class TopicMetadata {

    public String topicId;
    public String displayName;
    public List<String> tags;
    public List<Section> sections;
    public String difficulty; // "beginner", "intermediate", "advanced"

    public static class Section {
        public String name;
        public String description;
        public List<String> questionIds; // References to questions in this section

        public Section() {
        }

        public Section(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public TopicMetadata() {
    }

    public TopicMetadata(String topicId, String displayName, List<String> tags, String difficulty) {
        this.topicId = topicId;
        this.displayName = displayName;
        this.tags = tags;
        this.difficulty = difficulty;
    }

    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    public Section getSection(String sectionName) {
        if (sections == null)
            return null;
        for (Section section : sections) {
            if (section.name.equals(sectionName)) {
                return section;
            }
        }
        return null;
    }
}
