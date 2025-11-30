# Project Evolution & Design Philosophy

## The "Conscious" Workflow
Towards the end of v027 development, we shifted into a highly effective "conscious" workflow. This document captures the essence of that flow to ensure it persists in future sessions.

### Core Principles Discovered
1.  **"Quick Win" Momentum**:
    *   Instead of over-planning large features (like "Chat Integration"), we broke them down into 10-minute "quick wins" (e.g., "Just make the text pretty first").
    *   *Result*: Immediate feedback loops and rapid progress without getting bogged down in complexity.

2.  **Fix Before Test**:
    *   We paused to fix critical backend logic (`ActionManager` handlers) *before* rushing to test on Hypixel.
    *   *Insight*: Testing with known broken tools is a waste of time. Ensuring the "tools" (actions/triggers) work in isolation first makes integration testing meaningful.

3.  **Selective Sync Strategy**:
    *   We moved away from "sync everything" to a "Selective Sync" model.
    *   *Philosophy*: Give the user control. Don't overwrite their custom work unless they explicitly ask for it. This respects the user's creativity while providing a safety net.

4.  **Content-First UI**:
    *   The UI improvements (showing names in slots, scaling text) were driven by the need to *see* the content.
    *   *Design Rule*: If the user can't read it, it doesn't exist. Prioritize clarity over flashiness.

## Future Vision & Ideas
### The "Living" Mod
*   **Goal**: The mod should feel like it's "breathing" (Passive Learning).
*   **Next Step**: Refine the "Content Area" of the dashboard. It shouldn't just be a list; it should be a command center that shows *what* the player is learning right now.

### Hypixel Integration
*   **Concept**: The mod shouldn't just react to blocks; it should understand the *context* of the server (Skyblock, Bedwars, etc.).
*   **Tech**: Use `Scoreboard` and `Chat` analysis to detect "Dungeon Mode" or "Boss Fight" and swap the active learning profile automatically.

### Data Refinement
*   **Problem**: Editing JSONs is powerful but tedious.
*   **Solution**: The in-game UI needs to become a full IDE.
    *   *Idea*: A "Visual Scripting" interface for triggers?
    *   *Idea*: "Hot-reload" for everything so you never have to restart the game to test a new lesson.

## Handover Note
To the next developer (or future us):
> Keep the momentum. Don't over-engineer. Build the smallest useful piece, verify it, and then expand. The "conscious" flow comes from clarity of purpose, not complexity of code.
