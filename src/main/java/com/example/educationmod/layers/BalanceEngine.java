package com.example.educationmod.layers;

import java.util.ArrayList;
import java.util.List;

/**
 * BalanceEngine - Auto-validates and balances the learning system
 * 
 * Checks new concepts against existing layers.
 * Detects gaps in foundation knowledge.
 * Handles conflicts and misconceptions.
 */
public class BalanceEngine {

    private static BalanceEngine INSTANCE;
    private final LayerManager layerManager;

    private BalanceEngine() {
        this.layerManager = LayerManager.getInstance();
    }

    public static BalanceEngine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BalanceEngine();
        }
        return INSTANCE;
    }

    /**
     * Validate if a concept can be learned (has prerequisites)
     */
    public ValidationResult validateConcept(String layerId) {
        ConceptLayer layer = findLayer(layerId);
        if (layer == null) {
            return new ValidationResult(false, "Layer not found", new ArrayList<>());
        }

        List<String> missing = layerManager.getMissingPrerequisites(layerId);
        if (missing.isEmpty()) {
            return new ValidationResult(true, "Ready to learn", new ArrayList<>());
        }

        return new ValidationResult(false, "Missing prerequisites", missing);
    }

    /**
     * Detect foundation gaps for a topic
     */
    public List<String> detectGaps(String topic) {
        List<String> gaps = new ArrayList<>();
        List<ConceptLayer> learned = layerManager.getLearnedLayers(topic);

        // Check if foundation layers are learned
        List<ConceptLayer> allLayers = layerManager.getTopicLayers().get(topic);
        if (allLayers == null) {
            return gaps;
        }

        for (ConceptLayer layer : allLayers) {
            if (layer.isFoundation() && !learned.contains(layer)) {
                gaps.add(layer.getId());
            }
        }

        return gaps;
    }

    /**
     * Get recommended next layer based on current progress
     */
    public ConceptLayer getRecommendedLayer(String topic) {
        // First, check for foundation gaps
        List<String> gaps = detectGaps(topic);
        if (!gaps.isEmpty()) {
            return findLayer(gaps.get(0)); // Learn foundation first
        }

        // Otherwise, get next sequential layer
        return layerManager.getNextLayer(topic);
    }

    /**
     * Check if learning path is balanced (no skipped layers)
     */
    public boolean isPathBalanced(String topic) {
        List<ConceptLayer> learned = layerManager.getLearnedLayers(topic);
        List<ConceptLayer> allLayers = layerManager.getTopicLayers().get(topic);

        if (allLayers == null) {
            return true;
        }

        // Check for gaps in sequence
        for (int i = 0; i < allLayers.size(); i++) {
            ConceptLayer layer = allLayers.get(i);
            if (!learned.contains(layer)) {
                // Found a gap - check if it's a prerequisite for learned layers
                for (ConceptLayer learnedLayer : learned) {
                    if (learnedLayer.getPrerequisites().contains(layer.getId())) {
                        return false; // Imbalanced - learned advanced without foundation
                    }
                }
            }
        }

        return true;
    }

    private ConceptLayer findLayer(String layerId) {
        for (List<ConceptLayer> layers : layerManager.getTopicLayers().values()) {
            for (ConceptLayer layer : layers) {
                if (layer.getId().equals(layerId)) {
                    return layer;
                }
            }
        }
        return null;
    }

    /**
     * Validation result for concept learning
     */
    public static class ValidationResult {
        public final boolean valid;
        public final String message;
        public final List<String> missingPrerequisites;

        public ValidationResult(boolean valid, String message, List<String> missingPrerequisites) {
            this.valid = valid;
            this.message = message;
            this.missingPrerequisites = missingPrerequisites;
        }
    }
}
