package com.example.educationmod.gui;

import com.example.educationmod.ModConfigManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuizEditorScreen extends Screen {
    private final File file;
    private ModConfigManager.TopicDefinition topic;
    private TextFieldWidget questionField;
    private TextFieldWidget answerField;
    private TextFieldWidget wrong1Field;
    private TextFieldWidget wrong2Field;
    private TextFieldWidget wrong3Field;
    private int currentQuestionIndex = 0;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public QuizEditorScreen(File file) {
        super(Text.literal("Quiz Editor: " + file.getName()));
        this.file = file;
        this.topic = ModConfigManager.loadTopic(file.getAbsolutePath());
        if (this.topic == null) {
            this.topic = new ModConfigManager.TopicDefinition();
            this.topic.questions = new ArrayList<>();
        }
        if (this.topic.questions == null) {
            this.topic.questions = new ArrayList<>();
        }
        if (this.topic.questions.isEmpty()) {
            addEmptyQuestion();
        }
    }

    private void addEmptyQuestion() {
        ModConfigManager.QuestionDefinition q = new ModConfigManager.QuestionDefinition();
        q.question = "New Question";
        q.correct_answer = "Correct";
        q.incorrect_answers = new ArrayList<>();
        q.incorrect_answers.add("Wrong 1");
        q.incorrect_answers.add("Wrong 2");
        q.incorrect_answers.add("Wrong 3");
        this.topic.questions.add(q);
    }

    @Override
    protected void init() {
        int y = 40;
        int center = this.width / 2;

        // Question
        this.addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> {
            if (currentQuestionIndex > 0) {
                saveCurrentQuestion();
                currentQuestionIndex--;
                init();
            }
        }).dimensions(center - 120, 10, 20, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> {
            if (currentQuestionIndex < topic.questions.size() - 1) {
                saveCurrentQuestion();
                currentQuestionIndex++;
                init();
            }
        }).dimensions(center + 100, 10, 20, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
            saveCurrentQuestion();
            addEmptyQuestion();
            currentQuestionIndex = topic.questions.size() - 1;
            init();
        }).dimensions(center + 130, 10, 20, 20).build());

        // Save & Close
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), b -> saveFile())
                .dimensions(this.width - 110, 10, 40, 20).build());

        // Discard (Close without saving)
        this.addDrawableChild(ButtonWidget.builder(Text.literal("X"), b -> this.close())
                .dimensions(this.width - 60, 10, 20, 20).build());

        ModConfigManager.QuestionDefinition q = topic.questions.get(currentQuestionIndex);

        this.questionField = new TextFieldWidget(this.textRenderer, center - 100, y, 200, 20, Text.literal("Question"));
        this.questionField.setMaxLength(256);
        this.questionField.setText(q.question);
        this.addDrawableChild(this.questionField);
        y += 30;

        this.answerField = new TextFieldWidget(this.textRenderer, center - 100, y, 200, 20,
                Text.literal("Correct Answer"));
        this.answerField.setMaxLength(128);
        this.answerField.setText(q.correct_answer);
        this.addDrawableChild(this.answerField);
        y += 30;

        this.wrong1Field = new TextFieldWidget(this.textRenderer, center - 100, y, 200, 20,
                Text.literal("Wrong Answer 1"));
        this.wrong1Field.setText(q.incorrect_answers.size() > 0 ? q.incorrect_answers.get(0) : "");
        this.addDrawableChild(this.wrong1Field);
        y += 25;

        this.wrong2Field = new TextFieldWidget(this.textRenderer, center - 100, y, 200, 20,
                Text.literal("Wrong Answer 2"));
        this.wrong2Field.setText(q.incorrect_answers.size() > 1 ? q.incorrect_answers.get(1) : "");
        this.addDrawableChild(this.wrong2Field);
        y += 25;

        this.wrong3Field = new TextFieldWidget(this.textRenderer, center - 100, y, 200, 20,
                Text.literal("Wrong Answer 3"));
        this.wrong3Field.setText(q.incorrect_answers.size() > 2 ? q.incorrect_answers.get(2) : "");
        this.addDrawableChild(this.wrong3Field);
    }

    private void saveCurrentQuestion() {
        ModConfigManager.QuestionDefinition q = topic.questions.get(currentQuestionIndex);
        q.question = this.questionField.getText();
        q.correct_answer = this.answerField.getText();
        q.incorrect_answers = new ArrayList<>();
        if (!this.wrong1Field.getText().isEmpty())
            q.incorrect_answers.add(this.wrong1Field.getText());
        if (!this.wrong2Field.getText().isEmpty())
            q.incorrect_answers.add(this.wrong2Field.getText());
        if (!this.wrong3Field.getText().isEmpty())
            q.incorrect_answers.add(this.wrong3Field.getText());
    }

    private void saveFile() {
        saveCurrentQuestion();
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(topic, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Gradient Background (Dark Blue to Purple)
        context.fillGradient(0, 0, this.width, this.height, 0xFF100010, 0xFF300030);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);

        // Show Flag Status
        ModConfigManager.QuestionDefinition q = topic.questions.get(currentQuestionIndex);
        if (q.flags != null && q.flags.contains("NEEDS_REVIEW")) {
            context.drawTextWithShadow(this.textRenderer, "FLAGGED FOR REVIEW", this.width / 2 + 120, 45, 0xFF5555);
        }

        context.drawTextWithShadow(this.textRenderer, "Question:", this.width / 2 - 150, 45, 0xAAAAAA);
        context.drawTextWithShadow(this.textRenderer, "Correct:", this.width / 2 - 150, 75, 0x00FF00);
        context.drawTextWithShadow(this.textRenderer, "Wrong:", this.width / 2 - 150, 105, 0xFF0000);
        super.render(context, mouseX, mouseY, delta);
    }
}
