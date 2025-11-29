
### Phase 1: GUI UX Improvements (Explicit Edit/Delete)
#### [MODIFY] [TriggerDashboardScreen.java](file:///c:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/gui/TriggerDashboardScreen.java)
- **Problem**: Right-click/Shift-Right-click is hidden/unknown to user.
- **Solution**: Render small `[E]` (Edit) and `[X]` (Delete) buttons next to each saved event in the sidebar.
- **Layout**: `[Icon] [Trigger Name]   [E] [X]`
### Phase 1: GUI UX Improvements (Explicit Edit/Delete)
#### [MODIFY] [TriggerDashboardScreen.java](file:///c:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/gui/TriggerDashboardScreen.java)
- **Problem**: Right-click/Shift-Right-click is hidden/unknown to user.
- **Solution**: Render small `[E]` (Edit) and `[X]` (Delete) buttons next to each saved event in the sidebar.
- **Layout**: `[Icon] [Trigger Name]   [E] [X]`

### Phase 3: Context-Aware Quiz Delivery
#### [MODIFY] [QuizManager.java](file:///c:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/src/main/java/com/example/educationmod/QuizManager.java)
- **Fix**: Ensure `startQuiz` checks if the quiz topic is relevant to the trigger.
- **Logic**: Add tags to Quizzes (e.g., "mining", "combat"). Match with Trigger tags.

## Phase 7: Library Sync (Git Integration)
- [x] **Structure**: Create `src/main/resources/library` folders.
- [ ] **Backend**: Update `importLibrary()` to accept flags (events, topics, courses).
- [ ] **UI**:
    - [x] Create `SyncScreen` with checkboxes for each category.
    - [x] Update "Sync" button on Dashboard to open `SyncScreen`.

## Phase 8: Skyblock Content & UI Fixes
- [x] **UI Fix**:
    - [x] Update `TriggerDashboardScreen` to render the name of the selected Trigger/Condition/Action inside or below its slot.
    - [x] Ensure the text updates immediately upon selection.
- [x] **Content Expansion**:
    - [x] **Triggers**: `SB_KILL_MOB` (Diamond Sword), `SB_GAIN_XP` (Exp Bottle), `SB_REGION_ENTER` (Compass).
    - [x] **Conditions**: `SB_LEVEL_REQ` (Book), `SB_ITEM_REQ` (Chest), `SB_PET_REQ` (Bone).
    - [x] **Actions**: `SB_GIVE_ITEM` (Emerald), `SB_PLAY_SOUND` (Note Block).

## Phase 9: Chat Integration & Polish (Quick Win)
- [x] **Chat Formatting**: Create `ChatUtils` for consistent, pretty mod messages.
- [x] **Action Execution**: Ensure `SEND_MESSAGE` uses the new formatting.
- [x] **Hypixel Prep**: Add a debug command to simulate server events.

## Next Steps (Planned)
- [ ] **Hypixel Integration Testing**
- [ ] **Chat UI Improvements**
- [ ] **Advanced Data Refinement UI**

## Verification Plan
### Manual Testing
1. **GUI**: Open Dashboard. Verify `[E]` and `[X]` buttons appear. Click them to confirm they work.
2. **Immersive**:
    - Enable Immersive Mode.
    - Break a Diamond Block.
    - Verify a proposal appears (Chat or Toast).
    - Accept it.
    - Verify it appears in the Dashboard.
3. **HUD**: Verify HUD remains visible (or correctly toggled) during gameplay.
