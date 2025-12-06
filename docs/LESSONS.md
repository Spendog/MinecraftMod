# 🎓 From Mining to Modding: The Bridge Course

**Source Material**: `aasic-runner` (Python/TypeScript Mining System)
**Target Application**: Minecraft Fabric Mod (Java/JSON Trigger Engine)

---

## 📌 Course Overview

You spent days debugging a complex distributed system. That wasn't wasted time—it was a crash course in systems architecture. This document translates those hard-learned lessons into direct implementation steps for your Minecraft mod.

---

## MODULE 1: The Observer Pattern (Event-Driven Architecture)

### 🧠 The Concept
**"Don't ask 'are we there yet?'. Wait for me to tell you."**
Instead of checking every tick "is the player holding a sword?", we wait for an event `PLAYER_ITEM_SWITCH`.

### ⛏️ Mining Project Example
**The Cheetah Sidecar**: It didn't constantly ask the miner "did you find a share?". It watched the log file. When a line appeared, it fired an event.
*   **Bug**: We crashed on emojis.
*   **Lesson**: Input sanitization is critical.

### 🎮 Minecraft Implementation (Fabric)
You need an **Event Bus**. Fabric has this built-in (`ServerTickEvents`, `AttackEntityCallback`).

**Your Task**: Create a `TriggerManager` that listens to Fabric events and checks your JSON triggers.

```java
// Java Concept (Fabric)
AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
    // 1. Construct the Event Context
    TriggerContext ctx = new TriggerContext("player_attack", player, entity);
    
    // 2. Pass to your Engine (The "Cheetah" equivalent)
    TriggerEngine.evaluate(ctx);
    
    return ActionResult.PASS;
});
```

**JSON Definition (Topic Format)**:
Save this in `mod_data/topics/combat_quiz.json`:
```json
{
  "topic": "Combat Basics",
  "triggers": [
    {
      "type": "player_attack",
      "target": "minecraft:zombie",
      "event": "trigger_quiz"
    }
  ],
  "questions": [
    {
      "question": "What is the best weapon against zombies?",
      "correct_answer": "Smite Sword",
      "incorrect_answers": ["Stick", "Feather", "Dirt"]
    }
  ]
}
```

---

## MODULE 2: Finite State Machines (AI Logic)

### 🧠 The Concept
**"You can't be Happy and Angry at the exact same time."**
Entities should have distinct "States". They behave differently in each state.

### ⛏️ Mining Project Example
**Strategy Engine**: `WARMUP` → `EXPLORATION` → `EXPLOITATION`.
*   **Bug**: We flipped states too fast (oscillating).
*   **Lesson**: **Hysteresis**. Once you enter a state, you must stay there for a minimum time (e.g., `lock_duration`).

### 🎮 Minecraft Implementation
Your "Trigger Engine" can give mobs custom AI states defined in JSON.

**Your Task**: Implement a `StateManager` that tracks the current "Mood" of an entity.

```java
// Java Concept
public class CustomEntityAI {
    private String currentState = "IDLE";
    private long lastStateChange = 0;

    public void tick() {
        // Enforce Hysteresis (The "Mining Lesson")
        if (System.currentTimeMillis() - lastStateChange < 2000) return;

        if (currentState.equals("IDLE") && playerNearby()) {
            transitionTo("ATTACK");
        }
    }
}
```

**JSON Definition**:
```json
{
  "state": "IDLE",
  "transitions": [
    { "trigger": "player_detected", "to": "ATTACK", "cooldown": 2.0 }
  ]
}
```

---

## MODULE 3: Data-Driven Design (Configuration)

### 🧠 The Concept
**"Hardcoding is a sin."**
If you have to recompile code to change a number (like `10000000` difficulty), you failed.

### ⛏️ Mining Project Example
**Config Files**: We moved `TRIGGER_10M` and `SWARM_SIZE` into `config.py`.
*   **Bug**: We had duplicate configs in `stratum_proxy.ts` and `strategy_engine.py`.
*   **Lesson**: **Single Source of Truth**.

### 🎮 Minecraft Implementation
Your mod should be driven entirely by JSON files in the `mod_data/` folder.

**Your Task**: Use GSON to load rules at startup.

```java
// Java Concept
public class ConfigLoader {
    public static ModConfig load() {
        try (Reader reader = Files.newBufferedReader(Paths.get("mod_data/config.json"))) {
            return new Gson().fromJson(reader, ModConfig.class);
        }
    }
}
```

---

## MODULE 4: Persistence (Saving Data)

### 🧠 The Concept
**"If it's not on disk, it didn't happen."**
In-memory variables die when the game crashes. You must serialize data.

### ⛏️ Mining Project Example
**Scoreboard**: We saved sector stats to `scoreboard.json`.
*   **Bug**: We changed the format (List -> Dict) and crashed the loader.
*   **Lesson**: **Versioning**. Always check "is this an old save file?" before loading.

### 🎮 Minecraft Implementation
Use **NBT Data** (Named Binary Tag) to save custom data to the world or entities.

**Your Task**: Save your "Trigger States" to the World NBT.

```java
// Java Concept (Fabric PersistentState)
public class TriggerWorldData extends PersistentState {
    public HashMap<String, Integer> globalCounters = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Serialize your counters
        nbt.putInt("zombies_killed", globalCounters.getOrDefault("zombies_killed", 0));
        return nbt;
    }
}
```

---

## MODULE 5: The "Safe" Workflow (Git & Debugging)

### 🧠 The Concept
**"Checkpoints save lives."**
You cannot build a complex system by just "coding forward". You must secure the ground you've won.

### ⛏️ Mining Project Example
**The Cycle**: Pressure → Change → Break → Fix.
*   **Failure**: We didn't tag a "stable" version before rewriting the proxy.
*   **Lesson**: **Commit often. Tag stable builds.**

### 🎮 Minecraft Implementation
**Your Task**:
1.  Initialize a Git repo for your mod.
2.  Before adding a new Trigger type, commit: `git commit -m "Stable: Attack triggers working"`
3.  If the new type breaks everything: `git reset --hard HEAD`

---

## 🚀 HOW TO SYNC THIS TO YOUR PROFILE

Since this file is in your **OneDrive**, it is already syncing to the cloud. To get it into your "Minecraft Edu Mod" git repository:

1.  **On your other device** (where the Minecraft repo is):
    *   Open the OneDrive folder.
    *   Locate `Mining/asics/aasic-runner/docs/minecraft_course/LESSONS.md`.
    *   **Copy** this file into your Minecraft Mod's `docs/` or `design/` folder.

2.  **Push to Git**:
    *   Open a terminal in your Minecraft Mod folder.
    *   Run:
        ```bash
        git add docs/LESSONS.md
        git commit -m "Add curriculum derived from Mining Project architecture"
        git push origin main
        ```

3.  **The Result**:
    *   You now have a permanent record of these lessons in your profile.
    *   You can reference this document while coding your mod.

---
*Generated by Antigravity | Dec 5, 2025*
