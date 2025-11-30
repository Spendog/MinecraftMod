# Documentation Restructure & In-Game Reflection

## Goal
1.  **Restructure `docs/`**: Separate stable/foundational documents from active/growth documents to protect the core while enabling "extravagant success".
2.  **In-Game Learning**: Add a JSON file describing this structure and expose it within the mod's gameplay (e.g., via the Course interface) so developers/players can learn about the project structure while playing.

## User Review Required
> [!IMPORTANT]
> **Folder Naming**: I am proposing `docs/foundation` for the "safe/don't edit" files and keeping active files in the root `docs/` or moving them to `docs/workspace`.
> **Proposed Split**:
> *   **Foundation (Safe)**: `project_evolution.md`, `design_evolution.md`, `DEVELOPER_ONBOARDING.md`, `handoff.md`
> *   **Workspace (Growth)**: `task.md`, `implementation_plan.md`, `walkthrough.md`

## Proposed Changes

### Documentation Workflow Rules
#### [UPDATE] `docs/foundation/DEVELOPER_ONBOARDING.md` & `README.md`
- **Safe Files Rule**: `project_evolution.md` and `design_evolution.md` are **APPEND-ONLY**. They record the history. Update them when major changes or design pivots occur.
- **Pre-Push Rule**: Before `git push`, explicitly review and upgrade `DEVELOPER_ONBOARDING.md` and `handoff.md` to ensure they match the current state.
- **README**: Update `README.md` to reflect the new `docs/foundation` vs `docs/workspace` structure and these workflow rules.

### Documentation Structure
#### [MODIFY] `docs/`
- Create `docs/foundation/`
- Move `project_evolution.md`, `design_evolution.md`, `DEVELOPER_ONBOARDING.md`, `handoff.md` to `docs/foundation/`
- Update `DEVELOPER_ONBOARDING.md` and `README.md` to reflect this new structure and the rules above.

### Resources
#### [NEW] `src/main/resources/assets/educationmod/courses/project_structure.json`
- A JSON file describing the new `docs` structure.
- Format compatible with the existing `BookScreen` or a new format if needed.
- Content:
  ```json
  {
    "title": "Project Architecture",
    "description": "Learn about the conscious structure of this mod.",
    "chapters": [
      {
        "title": "Foundation",
        "content": "The 'Foundation' folder contains the immutable core principles..."
      },
      {
        "title": "Growth",
        "content": "The 'Workspace' is where active development happens..."
      }
    ]
  }
  ```

### Java Code
#### [MODIFY] `com.example.educationmod.gui.BookScreen`
- Update logic to load `project_structure.json` as a special course or add it to the default course list.
- Ensure it renders correctly.

## Verification Plan
### Automated Tests
- None (Documentation and GUI visual check).

### Manual Verification
1.  **File System**: Verify `docs/foundation` exists and contains the correct files.
2.  **In-Game**: Open the "Courses" menu (or specific trigger).
3.  **Verify**: Check that "Project Architecture" (or similar) appears.
4.  **Verify**: Read the content in-game and confirm it matches the JSON.
