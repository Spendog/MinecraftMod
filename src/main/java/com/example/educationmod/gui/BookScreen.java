package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File; // Added import
import java.util.List;

public class BookScreen extends Screen {
    private final Screen parent;
    private final ModConfigManager.CourseDefinition course;
    private final ModConfigManager.BookDefinition book;

    // Constructor for Course List (Root)
    public BookScreen(Screen parent) {
        super(Text.literal("Available Courses"));
        this.parent = parent;
        this.course = null;
        this.book = null;
    }

    // Constructor for Book List (Inside a Course)
    public BookScreen(Screen parent, ModConfigManager.CourseDefinition course) {
        super(Text.literal("Course: " + course.title));
        this.parent = parent;
        this.course = course;
        this.book = null;
    }

    // Constructor for Chapter List (Inside a Book)
    public BookScreen(Screen parent, ModConfigManager.CourseDefinition course, ModConfigManager.BookDefinition book) {
        super(Text.literal("Book: " + book.title));
        this.parent = parent;
        this.course = course;
        this.book = book;
    }

    @Override
    protected void init() {
        int y = 60; // Increased top margin
        int center = this.width / 2;

        // Add Close Button (Top Right)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), button -> {
            this.close();
        }).dimensions(this.width - 30, 10, 20, 20).build());

        if (course == null) {
            // Show Courses
            List<ModConfigManager.CourseDefinition> courses = ModConfigManager.getCourses();
            if (courses.isEmpty()) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal("No Courses Found"), b -> {
                })
                        .dimensions(center - 100, y, 200, 20).build()).active = false;
            } else {
                for (ModConfigManager.CourseDefinition c : courses) {
                    this.addDrawableChild(ButtonWidget.builder(Text.literal(c.title), b -> {
                        this.client.setScreen(new BookScreen(this, c));
                    }).dimensions(center - 100, y, 200, 20).build());
                    y += 25;
                }
            }
        } else if (book == null) {
            // Show Books in Course
            for (ModConfigManager.BookDefinition b : course.books) {
                this.addDrawableChild(ButtonWidget.builder(Text.literal(b.title), button -> {
                    this.client.setScreen(new BookScreen(this, course, b));
                }).dimensions(center - 100, y, 200, 20).build());
                y += 25;
            }
        } else {
            // Show Chapters in Book
            for (ModConfigManager.ChapterDefinition ch : book.chapters) {
                // Main Chapter Button
                this.addDrawableChild(ButtonWidget.builder(Text.literal(ch.title), button -> {
                    if ("QUIZ".equals(ch.type)) {
                        // Load quiz
                        ModConfigManager.TopicDefinition topic = ModConfigManager.loadTopic(ch.content_file);
                        if (topic != null) {
                            this.client.setScreen(new QuizScreen(topic));
                        }
                    } else {
                        // Handle other types or error
                    }
                }).dimensions(center - 100, y, 170, 20).build()); // Reduced width to make room for Edit button

                // Edit Button (if it's a quiz)
                if ("QUIZ".equals(ch.type)) {
                    this.addDrawableChild(ButtonWidget.builder(Text.literal("E"), button -> {
                        File topicFile = ModConfigManager.TOPICS_DIR.resolve(ch.content_file).toFile();
                        this.client.setScreen(new QuizEditorScreen(topicFile));
                    }).dimensions(center + 75, y, 25, 20).build());
                }

                y += 25;
            }
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> this.client.setScreen(parent))
                .dimensions(center - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background (Dark Blue to Purple)
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
