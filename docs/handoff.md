# Handoff Document - v014 Complete

## System State
The **Layered Compositional Learning System** is fully operational.
- **Version**: v014
- **Build Status**: Success (`build/libs/MinecraftEDU_v1.21.5-v014.jar`)
- **Core Logic**: Triggers fire -> Quizzes appear -> Correct answers stack layers.

## Key Components

### 1. Layered Learning (`com.example.educationmod.layers`)
- **`ConceptLayer`**: Represents a single unit of understanding.
- **`LayerManager`**: Manages stacks, prerequisites, and reinforcement.
- **`BalanceEngine`**: Ensures learning paths are valid (no gaps).

### 2. Triggers & Quizzes (`com.example.educationmod`)
- **`TriggerRegistry`**: Tracks events (Block Break, Item Use) and statistics.
- **`ChatQuizHandler`**: Handles `/edu` command, validates answers, and calls `LayerManager`.
- **`IdleDetector`**: Triggers quizzes during gameplay pauses.

### 3. Data Structure (`com.example.educationmod`)
- **`TopicMetadata`**: Handles tags, sections, and difficulty levels.
- **`ModConfigManager`**: Loads JSONs from `config/mod_data/`.

### 4. GUI (`com.example.educationmod.gui`)
- **`DashboardScreen`**: Visualizes layer stacks and exports detailed logs.
- **`TriggerEditorScreen`**: Displays active triggers and firing history.

## Recent Changes (v014)
1.  **Fixed Chat Quiz**: `/edu` command now validates and shuffles answers properly.
2.  **Layer Integration**: Answering correctly stacks the corresponding concept layer.
3.  **Trigger Tracking**: Added statistics (counts/timestamps) to `TriggerRegistry`.
4.  **Enhanced Logging**: `DashboardScreen` exports full layer/trigger data to clipboard.

## Next Steps (v015)
The system is code-complete. The next phase is **Content & Polish**.

1.  **Content Expansion**:
    - Create full `geology.json` course with defined layers.
    - Expand `color_theory.json` with sections (Primary, Secondary, Mixing).
2.  **Visualizations**:
    - Add a visual graph of connected layers in `DashboardScreen`.
3.  **Audio**:
    - Add sound effects for correct/incorrect answers and layer stacking.

## How to Resume
1.  **Load Project**: Open `C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod`.
2.  **Verify**: Run `.\gradlew build` to ensure environment is clean.
3.  **Run**: Execute `.\gradlew runClient` to start the mod.
4.  **Test**:
    - Mine coal/diamond to fire triggers.
    - Wait for idle quiz.
    - Answer with `/edu` to see layers stack.

## Development Philosophy & "Rules of Engagement"
*Based on the successful v012-v014 development cycle.*

1.  **The "Living" System**: Just as the mod treats learning as a growing stack, development should be iterative. Do not build "dead" features; build "living" engines that read external data (JSON).
2.  **No Guesswork**: We succeeded by strictly following `task.md` and `implementation_plan.md`. If in doubt, stop and check the docs.
3.  **Layered Complexity**: We built the HUD (v012), then the Logic (v013), then the Integration (v014). Future updates should follow this "Foundation First" approach.
4.  **User-Centric**: The "3D Puzzle" metaphor came from user feedback. Always prioritize the *feel* of learning over raw mechanics.
5.  **Documentation is Code**: The `docs/` folder is not an afterthought. It is the save file. Update it *before* code.

## Final Note
This project is designed to grow. The `ModConfigManager` allows infinite content expansion without code changes. The `LayerManager` allows infinite depth. Keep the engine clean, and let the content drive the complexity.
