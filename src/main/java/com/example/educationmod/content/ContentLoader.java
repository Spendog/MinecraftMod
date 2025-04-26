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
    public static List<EducationalContent> contents = new ArrayList<>();
    
    public static void loadContent() {
        // Add some sample content
        contents.add(new EducationalContent("Mining", "Mining is essential for gathering resources"));
        contents.add(new EducationalContent("Crafting", "Crafting allows you to create new items"));
        contents.add(new EducationalContent("Building", "Building lets you create structures"));
        try {
            ResourceLocation resource = new ResourceLocation(EducationMod.MODID, "educontent.json");
            InputStreamReader reader = new InputStreamReader(
                    Minecraft.class.getResourceAsStream("/assets/educationmod/educontent.json")
            );
            Type listType = new TypeToken<ArrayList<EducationalContent>>(){}.getType();
            List<EducationalContent> loadedContent = new Gson().fromJson(reader, listType);
            if (loadedContent != null) {
                contents.addAll(loadedContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<EducationalContent> getContent() {
        return contents;
    }
    
    public static EducationalContent getRandomContent(String topic) {
        List<EducationalContent> matchingContent = contents.stream()
            .filter(content -> content.getTopic().equalsIgnoreCase(topic))
            .collect(Collectors.toList());
        
        if (matchingContent.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return matchingContent.get(random.nextInt(matchingContent.size()));
    }
}