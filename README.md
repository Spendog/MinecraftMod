# MinecraftMod: Layered Learning Engine (Fabric 1.21.5)

**Goal:** A "Layered Compositional Learning System" driven by external JSON files. It treats learning as a "3D puzzle" where concepts stack to build deep understanding.

## Core Philosophy
1.  **Immersive Learning**: Facts stream in naturally via HUD (v012).
2.  **Layered Concepts**: Understanding is built by stacking "concept layers" (v013).
3.  **Trigger-Event Engine**: Gameplay actions (mining, crafting) trigger learning moments (v014).

## Architecture

### 1. The Brain (Data)
**`ModConfigManager`** reads/writes to `.minecraft/config/mod_data/`.
*   **`topics/`**: Quiz Questions with tags, sections, and difficulty.
*   **`events/`**: Triggers (e.g., `mining_diamond.json`).

### 2. The Triggers (Events)
**`TriggerRegistry`** listens for Fabric Events:
*   **`BlockBreakEvent`**: Mining specific blocks.
*   **`UseItemEvent`**: Using specific items.
*   **`IdleDetector`**: Natural pauses in gameplay.

### 3. The Layers (Learning)
**`LayerManager`** tracks conceptual progress:
*   **Stacking**: Correct quiz answers add to the stack.
*   **Prerequisites**: Advanced layers require foundation layers.
*   **Reinforcement**: Repetition strengthens the stack.

### 4. The GUI (Interaction)
*   **`LearningHUD`**: Passive fact ticker and streak counter.
*   **`DashboardScreen`**: Visualizes layer stacks and learning progress.
*   **`TriggerEditorScreen`**: Shows active triggers and statistics.
*   **`ModMenuScreen`**: Central hub for all tools.

## Key Features (v014)
*   **Chat Quizzes**: `/edu` command for seamless answering.
*   **Layer Integration**: Correct answers stack concepts automatically.
*   **Trigger Tracking**: See exactly when and how often triggers fire.
*   **Detailed Logging**: Export learning data to clipboard for analysis.

## Implementation Roadmap
*   ✅ **v012**: Immersive Learning (HUD, Idle Quizzes)
*   ✅ **v013**: Layered System (Concept Stacks, Balance Engine)
*   ✅ **v014**: Integration (Triggers -> Quizzes -> Layers)
*   ⬜ **v015**: Content Expansion (Geology, Color Theory Deep Dive)
