package com.example.educationmod.gui;

import com.example.educationmod.course.CourseManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CourseScreen extends Screen {
    private final Screen parent;
    private final CourseManager.Course course;
    private CourseManager.Chapter currentChapter;
    private int chapterIndex = 0;

    public CourseScreen(Screen parent, String courseId) {
        super(Text.literal("Course: " + courseId));
        this.parent = parent;
        this.course = CourseManager.getCourse(courseId);
        if (this.course != null && !this.course.chapters.isEmpty()) {
            this.currentChapter = this.course.chapters.get(0);
        }
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int bottom = this.height - 30;

        // Navigation Buttons
        this.addDrawableChild(ButtonWidget.builder(Text.literal("< Prev"), button -> {
            if (chapterIndex > 0) {
                chapterIndex--;
                currentChapter = course.chapters.get(chapterIndex);
            }
        }).dimensions(center - 110, bottom, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Next >"), button -> {
            if (chapterIndex < course.chapters.size() - 1) {
                chapterIndex++;
                currentChapter = course.chapters.get(chapterIndex);
            }
        }).dimensions(center + 10, bottom, 100, 20).build());

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {
            this.close();
        }).dimensions(this.width - 60, 10, 50, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Background
        this.renderBackground(context, mouseX, mouseY, delta);
        context.fill(20, 20, this.width - 20, this.height - 20, 0xCC000000);

        if (course == null) {
            context.drawCenteredTextWithShadow(this.textRenderer, "Course not found!", this.width / 2, this.height / 2,
                    0xFF5555);
            super.render(context, mouseX, mouseY, delta);
            return;
        }

        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, "§6§l" + course.title, this.width / 2, 30, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "§7" + course.description, this.width / 2, 45, 0xAAAAAA);

        // Chapter Content
        if (currentChapter != null) {
            int y = 70;
            context.drawText(this.textRenderer, "§eChapter " + (chapterIndex + 1) + ": " + currentChapter.title, 40, y,
                    0xFFFF55, false);
            y += 20;

            for (CourseManager.Page page : currentChapter.pages) {
                context.drawText(this.textRenderer, "• " + page.title, 50, y, 0xFFFFFF, false);
                y += 12;
                context.drawText(this.textRenderer, "  " + page.content, 50, y, 0xCCCCCC, false);
                y += 20;
            }
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
