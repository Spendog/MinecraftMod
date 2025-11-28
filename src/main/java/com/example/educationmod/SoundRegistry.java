package com.example.educationmod;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class SoundRegistry {
    // For now, we map our "custom" sounds to vanilla sounds
    // In a real mod, we would register actual custom SoundEvents here

    public static SoundEvent QUIZ_CORRECT = SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
    public static SoundEvent QUIZ_INCORRECT = SoundEvents.BLOCK_NOTE_BLOCK_BASS.value();
    public static SoundEvent LAYER_COMPLETE = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
    public static SoundEvent LEVEL_UP = SoundEvents.ENTITY_PLAYER_LEVELUP;

    public static void init() {
        // No-op for now, but ready for real registration later
    }
}
