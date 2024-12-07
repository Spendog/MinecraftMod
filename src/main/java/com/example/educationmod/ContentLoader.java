package com.example.educationmod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ContentLoader {
    private static List<EducationalContent> contentList;

    public static void loadContent() {
        try (FileReader reader = new FileReader("resources/educontent.json")) {
            Type contentType = new TypeToken<List<EducationalContent>>() {}.getType();
            contentList = new Gson().fromJson(reader, contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<EducationalContent> getContent() {
        return contentList;
    }
}

class EducationalContent {
    private String topic;
    private String content;

    // Getters and setters omitted for brevity
}