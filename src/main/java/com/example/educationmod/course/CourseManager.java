package com.example.educationmod.course;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Course> COURSES = new HashMap<>();
    private static final File COURSES_DIR = FabricLoader.getInstance().getConfigDir().resolve("educationmod/courses")
            .toFile();

    public static void init() {
        if (!COURSES_DIR.exists()) {
            COURSES_DIR.mkdirs();
            createExampleCourse();
        }
        loadCourses();
    }

    private static void loadCourses() {
        COURSES.clear();

        // Load bundled courses (like Project Architecture)
        loadBundledCourse("project_structure");

        // Load user courses
        File[] files = COURSES_DIR.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try (FileReader reader = new FileReader(file)) {
                    Course course = GSON.fromJson(reader, Course.class);
                    course.id = file.getName().replace(".json", "");
                    COURSES.put(course.id, course);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loadBundledCourse(String name) {
        try (java.io.InputStream stream = CourseManager.class
                .getResourceAsStream("/assets/educationmod/courses/" + name + ".json")) {
            if (stream != null) {
                java.io.InputStreamReader reader = new java.io.InputStreamReader(stream);
                Course course = GSON.fromJson(reader, Course.class);
                course.id = name;
                COURSES.put(course.id, course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Course getCourse(String id) {
        return COURSES.get(id);
    }

    public static void saveCourse(Course course) {
        COURSES.put(course.id, course);
        try (FileWriter writer = new FileWriter(new File(COURSES_DIR, course.id + ".json"))) {
            GSON.toJson(course, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createExampleCourse() {
        Course mining = new Course();
        mining.id = "course_mining";
        mining.title = "Mining Mastery";
        mining.description = "Learn the basics of mining ores and minerals.";

        Chapter basics = new Chapter();
        basics.title = "Basics of Mining";
        basics.pages.add(new Page("Intro", "Mining is essential for gathering resources."));
        basics.pages.add(new Page("Pickaxes", "Different ores require different pickaxes."));

        mining.chapters.add(basics);
        saveCourse(mining);
    }

    // Data Structures
    public static class Course {
        public String id;
        public String title;
        public String description;
        public List<Chapter> chapters = new ArrayList<>();
    }

    public static class Chapter {
        public String title;
        public List<Page> pages = new ArrayList<>();
    }

    public static class Page {
        public String title;
        public String content;
        public String quizId; // Optional link to a quiz

        public Page(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
