# MinecraftMod
for education

# MinecraftMod: Trigger-Event Engine (Fabric 1.21.5)

**Goal:** Porting a modular design to Fabric 1.21.5. A "Trigger-Event Engine" driven by external JSON files, designed for offline interaction with Hypixel Skyblock (local processing).

## Core Philosophy
The mod does not have hardcoded gameplay. It reads JSON files to decide what to watch for and what to do.

## Architecture

### 1. The Data Structure (The Brain)
**`ModConfigManager`** reads/writes to `.minecraft/config/mod_data/`.

*   **`topics/`**: Stores Quiz Questions (e.g., `math.json`, `renal_failure.json`).
*   **`events/`**: Stores Logic (e.g., `mining_diamond.json`, `slayer_spawn.json`).

### 2. The Event System (The Triggers)
**`TriggerRegistry`** listens for specific Fabric Events:

*   **`BlockBreakEvent`**: Condition: specific BlockID.
*   **`UseItemEvent`**: Condition: specific ItemID.
*   **`ClientChatReceivedEvent`**: Condition: Regex String Match (e.g., for Slayer Bosses).

### 3. The Action System (The Output)
When a Trigger fires (e.g., Chat detects "Revenant Horror"), execute the Action defined in the JSON:

*   **Action A (Quiz)**: Open `QuizScreen` (loads data from `topics/renal.json`).
*   **Action B (Input Lock)**: Cancel `AttackPacket` until Quiz is resolved.
*   **Action C (Command)**: Execute `/say Hello`.

### 4. The GUI (The Editor)
**`ModMenuScreen`** (accessed via `/modmenu`):

*   Lists all loaded JSON files.
*   *(Future Goal)* Allows editing these JSONs in-game.

## Implementation Roadmap
1.  **Setup**: Initialize Fabric 1.21.5 workspace.
2.  **Core**: Build `ModConfigManager` and `TriggerRegistry`.
3.  **Actions**: Implement the Action System and Quiz Logic.
4.  **UI**: Create the `ModMenuScreen`.
