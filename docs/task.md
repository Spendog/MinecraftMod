- [ ] **Build** <!-- id: 219 -->
    - [ ] Run `./gradlew build` <!-- id: 220 -->
- [x] Create `CourseManager` to handle Course -> Chapter -> Page structure.
- [x] Implement `OPEN_COURSE` action in `ActionManager`.
- [x] Create `CourseScreen` GUI to display course content.
- [ ] Verify Immersive Mode proposes courses based on context.

## Phase 5: Final Polish & Versioning
- [x] **Versioning**: Fix double version string in JAR name.
- [x] **Safe Mode UI**:
    - [x] Dashboard: Make Safe Mode indicator read-only.
    - [x] Settings: Add confirmation dialog when disabling Safe Mode.
- [x] **Dashboard UX**:
    - [x] Implement strict filtering based on selected slot (Trigger/Condition/Action).
    - [x] Auto-switch to Condition slot after selecting a Trigger.
- [x] **Crash Fix**:
    - [x] Move client-side initialization to `EducationModClient`.
    - [x] Clean up redundant registry initialization.

## Phase 6: Final UI Polish (Renaming & Text)
- [x] **Grid UI**: Improve visibility of item names (prevent aggressive truncation).
- [x] **Event Renaming**:
    - [x] Add `name` field to `EventDefinition` in `ModConfigManager`.
    - [x] Add "Event Name" input field to `TriggerDashboardScreen`.
    - [x] Update `saveEvent` to store the name.
    - [x] Update sidebar rendering to show the name.

## Phase 7: Library Sync (Git Integration)
- [x] **Structure**: Create `src/main/resources/library` folders (events, topics, courses).
- [x] **Backend**: Update `importLibrary()` to accept flags.
- [x] **UI**: Create `SyncScreen` with checkboxes and link to Dashboard.

## Phase 8: Skyblock Content & UI Fixes
- [x] **UI**: Fix selected item text not updating/visible in Dashboard slots.
- [x] **Triggers**: Add Skyblock-specific triggers (Kill Mob, Gain XP, Region).
- [x] **Conditions**: Add Skyblock-specific conditions (Level, Item, Pet).
- [x] **Actions**: Add Skyblock-specific actions (Give Item, Sound).

## Phase 9: Chat Integration & Polish (Quick Win)
- [x] **Chat Formatting**: Create `ChatUtils` for consistent, pretty mod messages.
- [x] **Action Execution**: Ensure `SEND_MESSAGE` uses the new formatting.
- [x] **Hypixel Prep**: Add a debug command to simulate server events.

## Phase 10: v028 Hotfix
- [x] **Crash Fix**: Correct `fabric.mod.json` structure (renamed `depends` to `entrypoints` by mistake).

## Future Goals (Post-v028)
- [ ] **Testing**: Begin testing on Hypixel server.
- [ ] **Data Refinement**: Make in-mod data editing more intuitive.
- [ ] **Content Area**: Refine the dashboard content area to show more relevant info.
- [ ] **Sync**: Explore auto-sync or better Git integration for builds.
