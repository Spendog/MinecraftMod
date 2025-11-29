# Education Mod (v027)

> **Philosophy**: Code is ephemeral; Ideas are persistent. We value **growth over stability**. We are willing to break a feature to reintroduce it in a more perfect form.

A Minecraft mod that turns gameplay into an immersive learning experience. The core is a data-driven engine where all content (quizzes, triggers, courses) lives in JSON files, safe from code refactors.

## 🚀 Quick Start (For Developers)
**Start Here**: [docs/project_evolution.md](docs/project_evolution.md) - Captures our "conscious" workflow and design philosophy.

### The "Conscious" Workflow
1.  **Quick Wins**: Break tasks into 10-minute chunks.
2.  **Fix Before Test**: Ensure tools work in isolation before integration testing.
3.  **Content First**: UI exists to serve the content.

## 🧬 The Evolution
This project follows a "Break & Reintroduce" cycle. We track the history of our features—from Concept to Good to Perfected—in [docs/design_evolution.md](docs/design_evolution.md).

## Features (v027)
### 🧠 Adaptive Learning Engine
*   **Selective Sync**: Import default content (Events, Topics, Courses) without overwriting your custom work.
*   **Skyblock Integration**: Triggers for killing mobs, gaining XP, and entering regions.
*   **Chat Integration**: Beautifully formatted educational messages and action bar tips.

### 🛠️ Developer Tools
*   **Trigger Dashboard**: A full in-game IDE for creating learning triggers.
*   **Safe Mode**: Prevents dangerous actions while developing.
*   **Console Overlay**: Press `~` to open a real-time debug console.

## Installation
1.  Install **Fabric Loader** for Minecraft 1.21.4.
2.  Install **Fabric API**.
3.  Install **Mod Menu** (optional, for settings).
4.  Drop `MinecraftEDU_v1.21.4-v027.jar` into your `mods` folder.

## Usage
*   **Dashboard**: Press `K` (default) to open the Trigger Dashboard.
*   **Sync**: Click "Sync" in the dashboard to load default content.
*   **Console**: Press `~` to open the developer console.

## Credits
Developed by s&cgpt.
