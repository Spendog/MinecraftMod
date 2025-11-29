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
        // ... (Existing example files) ...
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

        // Color Theory Topics
        createFileIfNotExists(TOPICS_DIR.resolve("mixing_colors.json"), "{\n" +
                "  \"questions\": [\n" +
                "    { \"question\": \"Red + Blue = ?\", \"correct_answer\": \"Purple\", \"incorrect_answers\": [\"Green\", \"Orange\"] },\n"
                +
                "    { \"question\": \"Blue + Yellow = ?\", \"correct_answer\": \"Green\", \"incorrect_answers\": [\"Purple\", \"Orange\"] },\n"
                +
                "    { \"question\": \"Red + Yellow = ?\", \"correct_answer\": \"Orange\", \"incorrect_answers\": [\"Green\", \"Purple\"] }\n"
                +
                "  ]\n" +
                "}");

        createFileIfNotExists(TOPICS_DIR.resolve("hex_codes.json"), "{\n" +
                "  \"questions\": [\n" +
                "    { \"question\": \"Hex for White?\", \"correct_answer\": \"#FFFFFF\", \"incorrect_answers\": [\"#000000\", \"#888888\"] },\n"
                +
                "    { \"question\": \"Hex for Black?\", \"correct_answer\": \"#000000\", \"incorrect_answers\": [\"#FFFFFF\", \"#111111\"] },\n"
                +
                "    { \"question\": \"Hex for Red?\", \"correct_answer\": \"#FF0000\", \"incorrect_answers\": [\"#00FF00\", \"#0000FF\"] }\n"
                +
                "  \"description\": \"Master the art of color.\",\n" +
                "  \"books\": [\n" +
                "    {\n" +
                "      \"title\": \"Basics\",\n" +
                "      \"chapters\": [ { \"title\": \"Mixing\", \"content_file\": \"mixing_colors.json\", \"type\": \"QUIZ\" } ]\n"
                +
                "    },\n" +
                "    {\n" +
                "      \"title\": \"Digital\",\n" +
                "      \"chapters\": [ { \"title\": \"Hex Codes\", \"content_file\": \"hex_codes.json\", \"type\": \"QUIZ\" } ]\n"
                +
                "    }\n" +
                "  ]\n" +
                "}");

        // Geology Course
        createFileIfNotExists(COURSES_DIR.resolve("geology.json"), "{\n" +
                "  \"id\": \"geology\",\n" +
                "  \"title\": \"Geology 101\",\n" +
                "  \"description\": \"Study the earth beneath your feet.\",\n" +
                "  \"books\": [\n" +
                "    {\n" +
                "      \"title\": \"Fundamentals\",\n" +
                "      \"chapters\": [ { \"title\": \"Rock Types\", \"content_file\": \"geology_basics.json\", \"type\": \"QUIZ\" } ]\n"
                +
                "    }\n" +
                "  ]\n" +
                "}");

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

    private static void createFileIfNotExists(Path path, String content) {
        File file = path.toFile();
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            } catch (IOException e) {
                EducationMod.LOGGER.error("Failed to create file " + file.getName(), e);
            }
        }
    }

    // Simple storage for events: TriggerType -> List of EventData
    // For prototype, we just store raw JSON objects or a simple class
    public static class EventDefinition {
        public String name;
        public String trigger;
        public String condition;
        public ActionDefinition action;
    }

    public static class ActionDefinition {
        public String type;
        public String data;
    }

    public static void loadEvents() {
        com.example.educationmod.core.RelationalStore.getInstance().clear(); // Clear DB before loading

        // Load Topics First (Dependencies)
        loadTopics();

        // Load Events
        File[] files = EVENTS_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    EventDefinition event = GSON.fromJson(reader, EventDefinition.class);
                    if (event != null) {
                        com.example.educationmod.core.RelationalStore.getInstance().addEvent(event);
                    }
                } catch (IOException e) {
                    EducationMod.LOGGER.error("Failed to load event " + file.getName(), e);
                }
            }
        }
    }

    public static void loadTopics() {
        File[] files = TOPICS_DIR.toFile().listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    TopicDefinition topic = GSON.fromJson(reader, TopicDefinition.class);
                    if (topic != null) {
                        topic.id = file.getName().replace(".json", "");
                        com.example.educationmod.core.RelationalStore.getInstance().addTopic(topic);
                    }
                } catch (IOException e) {
                    EducationMod.LOGGER.error("Failed to load topic " + file.getName(), e);
                }
            }
        }
    }

    public static List<EventDefinition> getEvents() {
        return com.example.educationmod.core.RelationalStore.getInstance().getAllEvents();
    }

    public static void saveEvent(EventDefinition event) {
        try {
            // Add to in-memory store
            com.example.educationmod.core.RelationalStore.getInstance().addEvent(event);

            // Generate unique filename
            String fileName = event.trigger.toLowerCase() + "_" + System.currentTimeMillis() + ".json";
            File eventFile = EVENTS_DIR.resolve(fileName).toFile();

            // Debug: Log save attempt
            EducationMod.LOGGER.info("Saving event to: " + eventFile.getAbsolutePath());
            ActivityLogger.log("Saving event: " + event.trigger + " -> " + event.action.type);

            // Write JSON
            try (FileWriter writer = new FileWriter(eventFile)) {
                GSON.toJson(event, writer);
                EducationMod.LOGGER.info("Event saved successfully: " + fileName);
            }
        } catch (IOException e) {
            EducationMod.LOGGER.error("Failed to save event", e);
            ActivityLogger.log("ERROR: Failed to save event - " + e.getMessage());
        }
    }

    public static void deleteEvent(int index) {
        try {
            List<EventDefinition> events = getEvents();
            if (index >= 0 && index < events.size()) {
                EventDefinition event = events.get(index);

                // Find and delete the corresponding file
                File[] eventFiles = EVENTS_DIR.toFile().listFiles((dir, name) -> name.endsWith(".json"));
                if (eventFiles != null && eventFiles.length > index) {
                    File fileToDelete = eventFiles[index];
                    if (fileToDelete.delete()) {
                        EducationMod.LOGGER.info("Deleted event file: " + fileToDelete.getName());
                        ActivityLogger.log("Deleted event: " + event.trigger);

                        // Remove from in-memory store and reload
                        loadEvents();
                    } else {
                        EducationMod.LOGGER.error("Failed to delete event file: " + fileToDelete.getName());
                    }
                }
            }
        } catch (Exception e) {
            EducationMod.LOGGER.error("Failed to delete event", e);
            ActivityLogger.log("ERROR: Failed to delete event - " + e.getMessage());
        }
    }

    public static TopicDefinition loadTopic(String filename) {
        // Redirect to Store
        return com.example.educationmod.core.RelationalStore.getInstance().getTopic(filename.replace(".json", ""));
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
        public String explanation; // Added explanation field
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

    public static void importLibrary(boolean importEvents, boolean importTopics, boolean importCourses) {
        EducationMod.LOGGER.info("Starting Library Sync...");

        if (importEvents)
            importFolder("events", EVENTS_DIR);
        if (importTopics)
            importFolder("topics", TOPICS_DIR);
        if (importCourses)
            importFolder("courses", COURSES_DIR);

        // Reload everything
        loadEvents();
        loadCourses();
        EducationMod.LOGGER.info("Library Sync Complete.");
    }

    private static void importFolder(String subFolder, Path targetDir) {
        FabricLoader.getInstance().getModContainer(EducationMod.MOD_ID).ifPresent(container -> {
            container.findPath("library/" + subFolder).ifPresent(sourceDir -> {
                try {
                    Files.walk(sourceDir).forEach(sourcePath -> {
                        if (Files.isRegularFile(sourcePath)) {
                            String fileName = sourcePath.getFileName().toString();
                            if (fileName.endsWith(".json")) {
                                Path targetPath = targetDir.resolve(fileName);
                                try {
                                    // Copy and overwrite
                                    Files.copy(sourcePath, targetPath,
                                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                    EducationMod.LOGGER.info("Imported: " + fileName);
                                } catch (IOException e) {
                                    EducationMod.LOGGER.error("Failed to copy " + fileName, e);
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    EducationMod.LOGGER.error("Failed to list files in library/" + subFolder, e);
                }
            });
        });
    }
}
