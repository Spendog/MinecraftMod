package com.example.educationmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("mod_data");
    public static final Path TOPICS_DIR = CONFIG_DIR.resolve("topics");
    public static final Path EVENTS_DIR = CONFIG_DIR.resolve("events");
    public static final Path COURSES_DIR = CONFIG_DIR.resolve("courses");

    private static final List<EventDefinition> loadedEvents = new ArrayList<>();
    private static final List<CourseDefinition> loadedCourses = new ArrayList<>();

    public static void init() {
        try {
            Files.createDirectories(TOPICS_DIR);
            Files.createDirectories(EVENTS_DIR);
            Files.createDirectories(COURSES_DIR);
            EducationMod.LOGGER.info("Configuration directories initialized at " + CONFIG_DIR);

            // Create example files if empty
            createExampleFiles();

            // Load events and courses
            loadEvents();
            loadCourses();
        } catch (IOException e) {
            EducationMod.LOGGER.error("Failed to create configuration directories", e);
        }
    }

    private static void createExampleFiles() {
        File exampleTopic = TOPICS_DIR.resolve("example_quiz.json").toFile();
        if (!exampleTopic.exists()) {
            try (FileWriter writer = new FileWriter(exampleTopic)) {
                writer.write("{\n" +
                        "  \"questions\": [\n" +
                        "    {\n" +
                        "      \"question\": \"What is 2+2?\",\n" +
                        "      \"correct_answer\": \"4\",\n" +
                        "      \"incorrect_answers\": [\"3\", \"5\", \"6\"]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}");
            } catch (IOException e) {
                EducationMod.LOGGER.error("Failed to create example topic", e);
            }
        }

        File exampleEvent = EVENTS_DIR.resolve("example_event.json").toFile();
        if (!exampleEvent.exists()) {
            try (FileWriter writer = new FileWriter(exampleEvent)) {
                writer.write("{\n" +
                        "  \"trigger\": \"BLOCK_BREAK\",\n" +
                        "  \"condition\": \"block.minecraft.diamond_block\",\n" +
                        "  \"action\": {\n" +
                        "    \"type\": \"QUIZ\",\n" +
                        "    \"data\": \"example_quiz.json\"\n" +
                        "  }\n" +
                        "}");
            } catch (IOException e) {
                EducationMod.LOGGER.error("Failed to create example event", e);
            }
        }
    }

    // Simple storage for events: TriggerType -> List of EventData
    // For prototype, we just store raw JSON objects or a simple class
    public static class EventDefinition {
        public String trigger;
        public String condition;
        public ActionDefinition action;
    }

    public static class ActionDefinition {
        public String type;
        public String data;
    }

    public static void loadEvents() {
        loadedEvents.clear();
        File[] files = EVENTS_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    EventDefinition event = GSON.fromJson(reader, EventDefinition.class);
                    if (event != null) {
                        loadedEvents.add(event);
                        EducationMod.LOGGER.info("Loaded event from " + file.getName());
                    }
                } catch (IOException e) {
                    EducationMod.LOGGER.error("Failed to load event " + file.getName(), e);
                }
            }
        }
    }

    public static List<EventDefinition> getEvents() {
        return loadedEvents;
    }

    public static TopicDefinition loadTopic(String filename) {
        File file = TOPICS_DIR.resolve(filename).toFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                TopicDefinition topic = GSON.fromJson(reader, TopicDefinition.class);
                if (topic != null) {
                    topic.id = filename.replace(".json", ""); // Set ID from filename
                }
                return topic;
            } catch (IOException e) {
                EducationMod.LOGGER.error("Failed to load topic " + filename, e);
            }
        }
        return null;
    }

    public static class TopicDefinition {
        public String id; // Added ID field
        public List<QuestionDefinition> questions;
    }

    public static class QuestionDefinition {
        public String question;
        public List<String> incorrect_answers;
        public String correct_answer;
        public List<String> flags; // Added flags field
    }

    // --- Course Structure ---

    public static class CourseDefinition {
        public String id;
        public String title;
        public String description;
        public List<BookDefinition> books = new ArrayList<>();
    }

    public static class BookDefinition {
        public String title;
        public List<ChapterDefinition> chapters = new ArrayList<>();
    }

    public static class ChapterDefinition {
        public String title;
        public String content_file; // Points to a Topic (JSON) or Text file
        public String type; // "QUIZ", "TEXT", "VIDEO" (external link)
    }

    public static void loadCourses() {
        loadedCourses.clear();
        File[] files = COURSES_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    CourseDefinition course = GSON.fromJson(reader, CourseDefinition.class);
                    if (course != null) {
                        loadedCourses.add(course);
                        EducationMod.LOGGER.info("Loaded course: " + course.title);
                    }
                } catch (IOException e) {
                    EducationMod.LOGGER.error("Failed to load course " + file.getName(), e);
                }
            }
        }
    }

    public static List<CourseDefinition> getCourses() {
        return loadedCourses;
    }
}
