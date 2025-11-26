package com.example.educationmod.layers;

import java.util.ArrayList;
import java.util.List;

/**
 * ConceptLayer - Represents a single layer in the learning stack
 * 
 * Each concept is a "piece" that can stack on related concepts.
 * Finding the same piece again reinforces it (increases stack height).
 */
public class ConceptLayer {

    private final String id;
    private final String concept;
    private final List<String> prerequisites;
    private final List<String> reinforcementFacts;
    private int stackHeight;

    public ConceptLayer(String id, String concept, List<String> prerequisites, List<String> reinforcementFacts) {
        this.id = id;
        this.concept = concept;
        this.prerequisites = prerequisites != null ? prerequisites : new ArrayList<>();
        this.reinforcementFacts = reinforcementFacts != null ? reinforcementFacts : new ArrayList<>();
        this.stackHeight = 0;
    }

    /**
     * Stack this concept - increases reinforcement
     */
    public void stack() {
        this.stackHeight++;
    }

    /**
     * Check if this layer has the required prerequisites
     */
    public boolean hasPrerequisites(List<String> learnedLayers) {
        if (prerequisites.isEmpty()) {
            return true; // Foundation layer
        }
        return learnedLayers.containsAll(prerequisites);
    }

    /**
     * Get a reinforcement fact based on current stack height
     */
    public String getReinforcementFact() {
        if (reinforcementFacts.isEmpty()) {
            return concept;
        }
        int index = Math.min(stackHeight, reinforcementFacts.size() - 1);
        return reinforcementFacts.get(index);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getConcept() {
        return concept;
    }

    public List<String> getPrerequisites() {
        return new ArrayList<>(prerequisites);
    }

    public int getStackHeight() {
        return stackHeight;
    }

    public void setStackHeight(int height) {
        this.stackHeight = height;
    }

    public boolean isFoundation() {
        return prerequisites.isEmpty();
    }

    @Override
    public String toString() {
        return concept + " (Stack: " + stackHeight + ")";
    }
}
