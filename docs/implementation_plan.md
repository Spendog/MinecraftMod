# Implementation Plan - v014 (Integration & Fixes)

## Goal
Fix broken features and integrate the layered learning system with quizzes. Address: broken `/edu` chat command, trigger visibility, topic organization (tags/sections), and logging improvements.

## User Review Required

> [!IMPORTANT]
> **Broken Features to Fix:**
> 1. Chat quiz replies (`/edu` command) not working
> 2. Triggers not visible/working properly
> 3. Topic data too small - need tags/sections
> 4. Logs not accurate without layer representation

## Proposed Changes

### 1. Fix Chat Quiz System

#### [MODIFY] [ChatQuizHandler.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/ChatQuizHandler.java)
- **Debug**: Check if command is registering
- **Fix**: Ensure `/edu` command processes answers correctly
- **Test**: Verify feedback appears in chat

#### [MODIFY] [IdleDetector.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/IdleDetector.java)
- **Fix**: Ensure quiz questions appear in chat when idle
- **Verify**: Question format is correct

---

### 2. Improve Trigger Visibility

#### [MODIFY] [TriggerEditorScreen.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/gui/TriggerEditorScreen.java)
- **Add**: Status indicators (✓ Active, ○ Inactive)
- **Add**: Last triggered timestamp
- **Add**: Trigger count (how many times fired)
- **Polish**: Better formatting, color coding

#### [MODIFY] [TriggerRegistry.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/TriggerRegistry.java)
- **Add**: Logging when triggers fire
- **Add**: Statistics tracking (trigger counts)

---

### 3. Topic Organization (Tags & Sections)

#### [NEW] [TopicMetadata.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/TopicMetadata.java)
- **Tags**: Categorize topics (e.g., "science", "art", "math")
- **Sections**: Organize questions into sections
- **Difficulty**: Track difficulty levels
- **Example**:
  ```json
  {
    "topic": "color_theory",
    "tags": ["art", "science", "visual"],
    "sections": [
      {
        "name": "Primary Colors",
        "questions": [...]
      },
      {
        "name": "Color Mixing",
        "questions": [...]
      }
    ]
  }
  ```

#### [MODIFY] [ModConfigManager.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/ModConfigManager.java)
- **Add**: Load topic metadata
- **Add**: Section-based question selection

---

### 4. Integrate Layered Learning with Quizzes

#### [MODIFY] [QuizScreen.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/gui/QuizScreen.java)
- **On Correct Answer**: Stack the related concept layer
- **On Wrong Answer**: Show missing prerequisite layers
- **Add**: "Show Foundation" button → displays prerequisite concepts

#### [MODIFY] [PlayerStats.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/PlayerStats.java)
- **Integrate**: Connect quiz results to LayerManager
- **Track**: Which layers were stacked from quiz results
- **Track**: Guess rate vs informed choice rate

---

### 5. Enhanced Logging

#### [MODIFY] [DashboardScreen.java](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/gui/DashboardScreen.java)
- Build with `.\gradlew build`

### Manual Verification
1. **Chat Quiz**: Type `/edu A` after idle quiz appears, verify feedback
2. **Triggers**: Open TriggerEditorScreen, verify status indicators
3. **Topics**: Check DashboardScreen shows tags/sections
4. **Layers**: Answer quiz correctly, verify layer stacks
5. **Logging**: Export data, verify layer information included
