package com.example.educationmod.core;

import com.example.educationmod.ModConfigManager;
import com.example.educationmod.debug.DeveloperConsole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A strict, in-memory relational store for the mod's data.
 * Acts as a database, enforcing integrity and providing query methods.
 */
public class RelationalStore {
    private static final RelationalStore INSTANCE = new RelationalStore();

    // Tables
    private final Map<String, ModConfigManager.TopicDefinition> topics = new HashMap<>();
    private final Map<String, ModConfigManager.CourseDefinition> courses = new HashMap<>();

    // Indices
    // TriggerType -> Condition -> List<Event>
    private final Map<String, Map<String, List<ModConfigManager.EventDefinition>>> triggerIndex = new HashMap<>();

    public static RelationalStore getInstance() {
        return INSTANCE;
    }

    public void clear() {
        topics.clear();
        courses.clear();
        triggerIndex.clear();
        DeveloperConsole.getInstance().log("Database cleared.", 0xAAAAAA);
    }

    public void addTopic(ModConfigManager.TopicDefinition topic) {
        if (topic.id == null || topic.id.isEmpty()) {
            DeveloperConsole.getInstance().logError("Attempted to add topic with no ID.");
            return;
        }
        if (topics.containsKey(topic.id)) {
            DeveloperConsole.getInstance().logWarning("Duplicate topic ID: " + topic.id + ". Overwriting.");
        }
        topics.put(topic.id, topic);
        DeveloperConsole.getInstance().log("Added Topic: " + topic.id, 0x55FF55);
    }

    public ModConfigManager.TopicDefinition getTopic(String id) {
        return topics.get(id);
    }

    public void addCourse(ModConfigManager.CourseDefinition course) {
        if (course.id == null) {
            DeveloperConsole.getInstance().logError("Attempted to add course with no ID.");
            return;
        }

        // Validate references
        for (ModConfigManager.BookDefinition book : course.books) {
            for (ModConfigManager.ChapterDefinition chapter : book.chapters) {
                if ("QUIZ".equals(chapter.type)) {
                    if (!topics.containsKey(chapter.content_file.replace(".json", ""))) {
                        DeveloperConsole.getInstance().logError(
                                "Course '" + course.id + "' references missing topic: " + chapter.content_file);
                    }
                }
            }
        }

        courses.put(course.id, course);
        DeveloperConsole.getInstance().log("Added Course: " + course.id, 0x55FF55);
    }

    public void addEvent(ModConfigManager.EventDefinition event) {
        if (event.trigger == null || event.condition == null) {
            DeveloperConsole.getInstance().logError("Invalid event definition.");
            return;
        }

        // Validate Action
        if (event.action != null && "QUIZ".equals(event.action.type)) {
            String topicId = event.action.data.replace(".json", "");
            if (!topics.containsKey(topicId)) {
                DeveloperConsole.getInstance()
                        .logError("Event trigger '" + event.condition + "' references missing topic: " + topicId);
                // We still add it, but we logged the error.
            }
        }

        triggerIndex.computeIfAbsent(event.trigger, k -> new HashMap<>())
                .computeIfAbsent(event.condition, k -> new ArrayList<>())
                .add(event);

        DeveloperConsole.getInstance().log("Added Trigger: " + event.trigger + " -> " + event.condition, 0x55FFFF);
    }

    public List<ModConfigManager.EventDefinition> findEvents(String triggerType, String condition) {
        Map<String, List<ModConfigManager.EventDefinition>> conditions = triggerIndex.get(triggerType);
        if (conditions != null) {
            return conditions.get(condition);
        }
        return null;
    }

    public List<ModConfigManager.TopicDefinition> getAllTopics() {
        return new ArrayList<>(topics.values());
    }

    public List<ModConfigManager.CourseDefinition> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<ModConfigManager.EventDefinition> getAllEvents() {
        List<ModConfigManager.EventDefinition> allEvents = new ArrayList<>();
        for (Map<String, List<ModConfigManager.EventDefinition>> conditionMap : triggerIndex.values()) {
            for (List<ModConfigManager.EventDefinition> events : conditionMap.values()) {
                allEvents.addAll(events);
            }
        }
        return allEvents;
    }
}
