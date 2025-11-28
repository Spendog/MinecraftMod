package com.example.educationmod.layers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import com.example.educationmod.SoundRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * LayerManager - Manages the learning layer system
 * 
 * Tracks which concepts are stacked and how high.
 * Ensures prerequisites are met before advanced concepts.
 * Handles reinforcement when encountering the same concept.
 */
public class LayerManager {

    private static LayerManager INSTANCE;
    private static final Gson GSON = new Gson();

    // All available layers by topic
    private Map<String, List<ConceptLayer>> topicLayers = new HashMap<>();

    // Player's learned layers (layer ID -> stack height)
    private Map<String, Integer> learnedLayers = new HashMap<>();

    private LayerManager() {
        loadLayers();
        loadPlayerProgress();
    }

    public static LayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LayerManager();
        }
        return INSTANCE;
    }

    private void loadLayers() {
        // Load layer definitions from JSON files
        // For now, create hardcoded examples

        List<ConceptLayer> coalLayers = new ArrayList<>();
        coalLayers.add(new ConceptLayer(
                "coal_layer_1",
                "Coal is primarily carbon",
                new ArrayList<>(),
                Arrays.asList("Ancient plant matter", "Fossilization process")));
        coalLayers.add(new ConceptLayer(
                "coal_layer_2",
                "Coal forms under pressure",
                Arrays.asList("coal_layer_1"),
                Arrays.asList("Heat and pressure", "Millions of years", "Deep underground")));
        coalLayers.add(new ConceptLayer(
                "coal_layer_3",
                "Coal types by carbon content",
                Arrays.asList("coal_layer_2"),
                Arrays.asList("Lignite", "Bituminous", "Anthracite")));

        topicLayers.put("coal", coalLayers);

        // Color theory layers
        List<ConceptLayer> colorLayers = new ArrayList<>();
        colorLayers.add(new ConceptLayer(
                "color_layer_1",
                "Primary colors cannot be mixed",
                new ArrayList<>(),
                Arrays.asList("Red, Blue, Yellow", "Foundation of color")));
        colorLayers.add(new ConceptLayer(
                "color_layer_2",
                "Secondary colors from primaries",
                Arrays.asList("color_layer_1"),
                Arrays.asList("Orange, Green, Purple", "Mix two primaries")));

        topicLayers.put("color_theory", colorLayers);
    }

    private void loadPlayerProgress() {
        // Load from player stats file
        // For now, initialize empty
    }

    /**
     * Stack a concept - reinforces learning
     */
    public boolean stackConcept(String layerId) {
        // Find the layer
        ConceptLayer layer = findLayer(layerId);
        if (layer == null) {
            return false;
        }

        // Check prerequisites
        if (!layer.hasPrerequisites(new ArrayList<>(learnedLayers.keySet()))) {
            return false; // Missing prerequisites
        }

        // Stack it
        int currentHeight = learnedLayers.getOrDefault(layerId, 0);
        learnedLayers.put(layerId, currentHeight + 1);
        layer.setStackHeight(currentHeight + 1);

        // Play sound
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.playSound(SoundRegistry.LAYER_COMPLETE, 1.0f, 1.0f);
        }

        return true;
    }

    /**
     * Get the next layer to learn for a topic
     */
    public ConceptLayer getNextLayer(String topic) {
        List<ConceptLayer> layers = topicLayers.get(topic);
        if (layers == null) {
            return null;
        }

        for (ConceptLayer layer : layers) {
            if (!learnedLayers.containsKey(layer.getId())) {
                // Check if prerequisites are met
                if (layer.hasPrerequisites(new ArrayList<>(learnedLayers.keySet()))) {
                    return layer;
                }
            }
        }

        return null; // All layers learned or prerequisites not met
    }

    /**
     * Get all learned layers for a topic
     */
    public List<ConceptLayer> getLearnedLayers(String topic) {
        List<ConceptLayer> layers = topicLayers.get(topic);
        if (layers == null) {
            return new ArrayList<>();
        }

        List<ConceptLayer> learned = new ArrayList<>();
        for (ConceptLayer layer : layers) {
            if (learnedLayers.containsKey(layer.getId())) {
                learned.add(layer);
            }
        }

        return learned;
    }

    /**
     * Get missing prerequisites for a layer
     */
    public List<String> getMissingPrerequisites(String layerId) {
        ConceptLayer layer = findLayer(layerId);
        if (layer == null) {
            return new ArrayList<>();
        }

        List<String> missing = new ArrayList<>();
        for (String prereq : layer.getPrerequisites()) {
            if (!learnedLayers.containsKey(prereq)) {
                missing.add(prereq);
            }
        }

        return missing;
    }

    /**
     * Get stack height for a layer
     */
    public int getStackHeight(String layerId) {
        return learnedLayers.getOrDefault(layerId, 0);
    }

    /**
     * Get total layers learned across all topics
     */
    public int getTotalLayersLearned() {
        return learnedLayers.size();
    }

    private ConceptLayer findLayer(String layerId) {
        for (List<ConceptLayer> layers : topicLayers.values()) {
            for (ConceptLayer layer : layers) {
                if (layer.getId().equals(layerId)) {
                    return layer;
                }
            }
        }
        return null;
    }

    public Map<String, List<ConceptLayer>> getTopicLayers() {
        return new HashMap<>(topicLayers);
    }
}
