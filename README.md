# MinecraftMod: Layered Learning Engine (Fabric 1.21.5)

**Goal:** A "Layered Compositional Learning System" driven by external JSON files. It treats learning as a "3D puzzle" where concepts stack to build deep understanding.

## 🚀 Getting Started (For Developers)

**STOP! READ THIS FIRST.**

To ensure consistent growth and maintain the project's philosophy, please follow this workflow. Do not guess; follow the established path.

### 📂 Documentation & Handoff
All development documentation is located in the `docs/` folder.

1.  **`docs/handoff.md`**: **START HERE.** This file contains the current system state, recent changes, and immediate next steps. It is the "save file" for our development process.
2.  **`docs/task.md`**: The master checklist. Check this to see what is done and what is planned.
3.  **`docs/walkthrough.md`**: A historical record of features and how they work. Consult this to understand existing systems before building new ones.
4.  **`docs/implementation_plan.md`**: Technical details for the current/recent version.

### 🧠 Core Philosophy (The "Why")
*   **Immersive**: Learning happens *during* gameplay, not in a separate menu.
*   **Layered**: Concepts are "living pieces" that stack. You don't just "know" a fact; you build a foundation.
*   **Data-Driven**: The mod is an engine. The *content* lives in JSON files.

## Architecture Overview

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

## How to Run
1.  **Build**: `.\gradlew build`
2.  **Run Client**: `.\gradlew runClient`

---
*Maintained by the Antigravity Team. Validated for Fabric 1.21.5.*
