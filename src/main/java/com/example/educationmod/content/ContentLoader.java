package com.example.educationmod.content;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ContentLoader {
    private static List<EducationalContent> contentList = new ArrayList<>();

    public static void loadContent() {
        try {
            ResourceLocation resource = new ResourceLocation("educationmod", "educontent.json");
            InputStreamReader reader = new InputStreamReader(
                ContentLoader.class.getClassLoader().getResourceAsStream(resource.getPath())
            );

            Type contentType = new TypeToken<List<EducationalContent>>() {}.getType();
            contentList = new Gson().fromJson(reader, contentType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<EducationalContent> getContent() {
        return contentList;
    }
}
