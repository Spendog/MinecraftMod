    *   *Purpose*: The "Blueprint". Propose your technical changes here.
    *   *Check*: Have you identified breaking changes?

3.  **Phase 3: Execution** -> [docs/task.md](docs/task.md)
    *   *Purpose*: The "Checklist". Track your progress item by item.
    *   *Check*: Are you focused on one small task at a time?

4.  **Phase 4: Verification** -> [docs/walkthrough.md](docs/walkthrough.md)
    *   *Purpose*: The "Proof". Document what you built and how to test it.
    *   *Check*: Did you verify the fix *before* moving on?

**Protection Rule**: Never overwrite a document without summarizing its key insights into the next version or the Evolution doc. This ensures we never lose the "history of ideas."

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
