# Education Mod (v024)

> **Philosophy**: Code is ephemeral; Ideas are persistent. We value **growth over stability**. We are willing to break a feature to reintroduce it in a more perfect form.

A Minecraft mod that turns gameplay into an immersive learning experience. The core is a data-driven engine where all content (quizzes, triggers, courses) lives in JSON files, safe from code refactors.

## 🧬 The Evolution
This project follows a "Break & Reintroduce" cycle. We track the history of our features—from Concept to Good to Perfected—in [docs/design_evolution.md](docs/design_evolution.md).

## Features

### 🧠 Adaptive Learning Engine
*   **Text Input Quizzes**: Answer questions in chat using natural language (e.g., `/edu Photosynthesis`).
*   **Knowledge Gap Tracker**: The mod learns what you don't know and logs your mistakes.
*   **Study Sets**: Generate personalized study guides (JSON) based on your knowledge gaps.

### 🛠️ Developer Tools
*   **Console Overlay**: Press `~` to open a real-time debug console.
*   **Advanced Querying**: Use SQL-like commands (`select topics`, `select triggers`) to inspect the engine.
*   **Visual Debugging**: See triggers fire and quizzes load in real-time.

### ⚙️ Customization
*   **Mod Menu Support**: Configure settings via the standard "Mods" menu.
*   **Immersive Mode**: Hide the HUD for a cleaner experience.
*   **Safe Mode**: Disable dangerous triggers.

## Installation
1.  Install **Fabric Loader** for Minecraft 1.21.5.
2.  Install **Fabric API**.
3.  Install **Mod Menu** (optional, for settings).
4.  Drop `MinecraftEDU_v1.21.5-v024.jar` into your `mods` folder.

## Usage
*   **Quizzes**: Play normally. Triggers (like breaking blocks) will spawn quizzes.
*   **Answering**: Type `/edu <answer>` in chat.
*   **Console**: Press `~` to open the developer console.
*   **Study**: Type `study` in the console to see your knowledge gaps.

## Credits
Developed by s&cgpt.
