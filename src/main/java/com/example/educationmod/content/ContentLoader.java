package com.example.educationmod.content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.ResourceLocation;
import com.example.educationmod.EducationMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ContentLoader {
    public static List<QuizContent> contents = new ArrayList<>();
    
    public static void loadContent() {
        // Add some sample QuizContent
        contents.add(new QuizContent(
            "What is mining used for?",
            new String[] {"Gathering resources", "Flying", "Trading"},
            0 // correct option index (0 = "Gathering resources")
        ));
        contents.add(new QuizContent(
            "What does crafting allow you to do?",
            new String[] {"Create new items", "Destroy mobs", "Teleport"},
            0
        ));
        contents.add(new QuizContent(
            "What is building used for?",
            new String[] {"Creating structures", "Finding ores", "Fishing"},
            0
        ));
        
        try {
            ResourceLocation resource = new ResourceLocation(EducationMod.MODID, "educontent.json");
            InputStreamReader reader = new InputStreamReader(
                    Minecraft.class.getResourceAsStream("/assets/educationmod/educontent.json")
            );
            Type listType = new TypeToken<ArrayList<QuizContent>>(){}.getType();
            List<QuizContent> loadedContent = new Gson().fromJson(reader, listType);
            if (loadedContent != null) {
                contents.addAll(loadedContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<QuizContent> getContent() {
        return contents;
    }
    
    public static QuizContent getRandomContent(String topic) {
        List<QuizContent> matchingContent = contents.stream()
            .filter(content -> content.getQuestion().equalsIgnoreCase(topic))
            .collect(Collectors.toList());
        
        if (matchingContent.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return matchingContent.get(random.nextInt(matchingContent.size()));
    }
}
