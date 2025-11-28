package com.example.educationmod.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import com.example.educationmod.ModSettings;

import java.util.ArrayList;
import java.util.List;

public class DeveloperConsole {
    private static final DeveloperConsole INSTANCE = new DeveloperConsole();
    private boolean visible = false;
    private final List<LogMessage> messages = new ArrayList<>();
    private final StringBuilder inputBuffer = new StringBuilder();
    private static final int MAX_MESSAGES = 100;

    public static DeveloperConsole getInstance() {
        return INSTANCE;
    }

    private DeveloperConsole() {
        log("Developer Console Initialized.", 0x00FF00);
    }

    public void toggle() {
        boolean newState = !ModSettings.isConsoleEnabled();
        ModSettings.setConsoleEnabled(newState);
        visible = newState;
    }

    public boolean isVisible() {
        return visible;
    }

    public void log(String text) {
        log(text, 0xFFFFFF);
    }

    public void logError(String text) {
        log(text, 0xFF5555);
    }

    public void logWarning(String text) {
        log(text, 0xFFAA00);
    }

    public void log(String text, int color) {
        messages.add(new LogMessage(text, color));
        if (messages.size() > MAX_MESSAGES) {
            messages.remove(0);
        }
    }

    public void render(DrawContext context, int width, int height) {
        if (!visible)
            return;

        // Background
        context.fill(0, 0, width, height / 2, 0xCC000000);

        // Input Line
        int inputY = (height / 2) - 15;
        context.fill(0, inputY, width, inputY + 1, 0xFFFFFFFF);
        context.drawText(MinecraftClient.getInstance().textRenderer, "> " + inputBuffer.toString() + "_", 5, inputY + 4,
                0xFFFFFF, false);

        // Messages
        int y = inputY - 12;
        for (int i = messages.size() - 1; i >= 0; i--) {
            LogMessage msg = messages.get(i);
            context.drawText(MinecraftClient.getInstance().textRenderer, msg.text, 5, y, msg.color, false);
            y -= 12;
            if (y < 5)
                break;
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!visible)
            return false;
        inputBuffer.append(chr);
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!visible) {
            if (keyCode == GLFW.GLFW_KEY_GRAVE_ACCENT) { // Tilde key to open
                toggle();
                return true;
            }
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_GRAVE_ACCENT) { // Tilde key to close
            toggle();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            executeCommand(inputBuffer.toString());
            inputBuffer.setLength(0);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (inputBuffer.length() > 0) {
                inputBuffer.setLength(inputBuffer.length() - 1);
            }
            return true;
        }

        return true; // Consume all other keys while console is open
    }

    private void executeCommand(String command) {
        log("> " + command, 0xAAAAAA);

        String[] parts = command.split(" ");
        if (parts.length == 0)
            return;

        switch (parts[0].toLowerCase()) {
            case "help":
                log("Available commands:", 0xFFFF55);
                log("  help - Show this help", 0xFFFFFF);
                log("  clear - Clear console", 0xFFFFFF);
                log("  reload - Reload configuration (TODO)", 0xFFFFFF);
                break;
            case "clear":
                messages.clear();
                break;
            case "select":
                handleSelectCommand(command);
                break;
            case "export":
                handleExportCommand();
                break;
            case "testlog":
                log("Test Log Message: " + System.currentTimeMillis(), 0x00FF00);
                logWarning("Test Warning Message");
                logError("Test Error Message");
                break;
            case "study":
                handleStudyCommand(command);
                break;
            default:
                logError("Unknown command: " + parts[0]);
                break;
        }
    }

    private void handleExportCommand() {
        java.io.File logFile = new java.io.File("education_mod_log.txt");
        try (java.io.PrintWriter writer = new java.io.PrintWriter(logFile)) {
            for (LogMessage msg : messages) {
                writer.println(msg.text);
            }
            log("Logs exported to: " + logFile.getAbsolutePath(), 0x55FF55);
        } catch (java.io.IOException e) {
            logError("Failed to export logs: " + e.getMessage());
        }
    }

    private void handleStudyCommand(String command) {
        if (command.equals("study export")) {
            com.example.educationmod.core.KnowledgeGapTracker.getInstance().generateStudySet();
            log("Study set generation triggered.", 0x55FF55);
            return;
        }

        java.util.List<com.example.educationmod.core.KnowledgeGapTracker.MissedConcept> gaps = com.example.educationmod.core.KnowledgeGapTracker
                .getInstance().getGaps();

        if (gaps.isEmpty()) {
            log("No knowledge gaps recorded yet.", 0xAAAAAA);
        } else {
            log("--- Knowledge Gaps (" + gaps.size() + ") ---", 0xFFAA00);
            for (com.example.educationmod.core.KnowledgeGapTracker.MissedConcept gap : gaps) {
                log("[" + gap.topic + "] " + gap.question + " (You said: " + gap.userAnswer + ")", 0xFF5555);
            }
        }
    }

    private void handleSelectCommand(String command) {
        // Syntax: select <table_name> [where <field>=<value>]
        String[] parts = command.split(" where ");
        String queryPart = parts[0].trim();
        String wherePart = parts.length > 1 ? parts[1].trim() : null;

        String[] queryTokens = queryPart.split(" ");
        if (queryTokens.length < 2) {
            logError("Usage: select <topics|courses|triggers> [where field=value]");
            return;
        }

        String table = queryTokens[1].toLowerCase();
        com.example.educationmod.core.RelationalStore store = com.example.educationmod.core.RelationalStore
                .getInstance();

        // Parse where clause once
        String filterKey = null;
        String filterValue = null;
        if (wherePart != null) {
            String[] condition = wherePart.split("=");
            if (condition.length == 2) {
                filterKey = condition[0].trim();
                filterValue = condition[1].trim();
            } else {
                logError("Invalid where clause. Usage: where field=value");
                return;
            }
        }

        switch (table) {
            case "topics":
                List<com.example.educationmod.ModConfigManager.TopicDefinition> topics = store.getAllTopics();
                log("Found " + topics.size() + " topics:", 0x55FFFF);
                for (com.example.educationmod.ModConfigManager.TopicDefinition topic : topics) {
                    if (filterValue == null || topic.id.contains(filterValue)) {
                        log(" - " + topic.id + " (" + topic.questions.size() + " questions)", 0xFFFFFF);
                    }
                }
                break;
            case "courses":
                List<com.example.educationmod.ModConfigManager.CourseDefinition> courses = store.getAllCourses();
                log("Found " + courses.size() + " courses:", 0x55FFFF);
                for (com.example.educationmod.ModConfigManager.CourseDefinition course : courses) {
                    if (filterValue == null || course.id.contains(filterValue)) {
                        log(" - " + course.id + " (" + course.books.size() + " books)", 0xFFFFFF);
                    }
                }
                break;
            case "triggers":
                List<com.example.educationmod.ModConfigManager.EventDefinition> events = store.getAllEvents();
                int count = 0;
                log("Querying triggers...", 0x55FFFF);
                for (com.example.educationmod.ModConfigManager.EventDefinition event : events) {
                    boolean match = true;
                    if (filterKey != null) {
                        if (filterKey.equals("trigger") && !event.trigger.contains(filterValue))
                            match = false;
                        if (filterKey.equals("condition") && !event.condition.contains(filterValue))
                            match = false;
                    }

                    if (match) {
                        log(" - " + event.trigger + " -> " + event.condition, 0xFFFFFF);
                        count++;
                    }
                }
                log("Total matches: " + count, 0x55FFFF);
                break;
            default:
                logError("Unknown table: " + table);
        }
    }

    private static class LogMessage {
        String text;
        int color;

        LogMessage(String text, int color) {
            this.text = text;
            this.color = color;
        }
    }
}
