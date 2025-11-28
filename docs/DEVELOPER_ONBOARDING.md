# Developer Onboarding Guide

> **Purpose**: This document ensures every new developer (AI or human) can understand the entire codebase structure from the start and maintain quality without reminders.

## 🚀 Quick Start (First 5 Minutes)

### 1. Read These Files in Order
1. **[README.md](file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/README.md)** - User-facing features
2. **[handoff.md](file:///C:/Users/minin/.gemini/antigravity/brain/15ecde54-cf55-46e5-9163-da55811575b8/handoff.md)** - Architecture overview
3. **[task.md](file:///C:/Users/minin/.gemini/antigravity/brain/15ecde54-cf55-46e5-9163-da55811575b8/task.md)** - Current development status
4. **This file** - Development workflow

### 2. Verify Your Environment
```powershell
# Check Java version (must be >=21)
java -version

# Check Gradle works
cd C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod
.\gradlew.bat tasks

# Verify current version
cat gradle.properties | Select-String "mod_version"
```

### 3. Run Your First Build
```powershell
.\gradlew.bat build
# JAR will be at: build/libs/MinecraftEDU_v1.21.5-v024.jar
```

---

## 📐 Architecture (The Mental Model)

### Core Principle: Data-Driven Engine
**The mod is NOT hardcoded**. All content (quizzes, triggers, courses) lives in JSON files. The code is just an engine that reads and executes them.

### The Three Pillars

#### 1. **Relational Store** (`core/RelationalStore.java`)
- In-memory database that enforces data integrity
- **Rule**: Cannot load an Event that triggers a non-existent Quiz
- **Access**: Read-only via `getTopics()`, `getEvents()`, `getCourses()`

#### 2. **Developer Console** (`debug/DeveloperConsole.java`)
- Real-time debugging interface (press `~`)
- **Overlay Mode**: Renders on top of any screen
- **Commands**: `select`, `study`, `export`, `testlog`

#### 3. **Adaptive Learning Engine** (`core/KnowledgeGapTracker.java`)
- Logs incorrect answers
- Generates personalized study sets
- **Integration**: Hooks into `ChatQuizHandler` error flow

---

## 🔄 Development Workflow

### Phase 1: Planning (ALWAYS FIRST)
```markdown
1. Update task.md with new tasks (use `[/]` for in-progress)
2. Create/update implementation_plan.md
3. Get user approval via notify_user
```

**Critical Rule**: NEVER skip planning for non-trivial changes.

### Phase 2: Implementation
```markdown
1. Create feature branch mentally (track changes)
2. Write code following existing patterns
3. Update gradle.properties version if releasing
4. Run build: .\gradlew.bat build
5. Test manually in-game (see Verification Plan)
```

### Phase 3: Documentation
```markdown
1. Update walkthrough.md with what you built
2. Update handoff.md if architecture changed
3. Update README.md if user-facing features changed
4. Mark tasks complete in task.md
```

### Phase 4: Pre-Push Checklist
Run this **BEFORE every GitHub push**:

```powershell
# 1. Self-consistency check
.\scripts\pre-push-check.bat

# What it checks:
# - gradle.properties version matches task.md
# - All [x] tasks in task.md are documented in walkthrough.md
# - No duplicate version JARs in build/libs
# - README.md mentions current version
# - handoff.md version is current
```

---

## 🧠 Self-Check Protocol

Before submitting ANY work, answer these questions:

### Consistency Questions
- [ ] Does `gradle.properties` version match the latest completed task in `task.md`?
- [ ] Is there only ONE version JAR in `build/libs/` (not multiple old versions)?
- [ ] Does `README.md` mention the current version number?
- [ ] Does `handoff.md` reflect any architecture changes I made?
- [ ] Are all `[x]` completed tasks in `task.md` documented in `walkthrough.md`?

### Quality Questions
- [ ] Did I follow the Red/Green/Blue strategy? (See strategic_options.md)
- [ ] Did I test the feature manually? (See implementation_plan.md verification section)
- [ ] Will the next developer understand my changes from the documentation alone?
- [ ] Did I add comments for any non-obvious code?
- [ ] Did I update sample content if adding new features?

### Anti-Regression Questions
- [ ] Did I verify old features still work? (Build + manual test)
- [ ] Did I accidentally remove any working code?
- [ ] Did I break any existing data files (topics/events/courses)?

**Rule**: If you answer "No" or "Unsure" to ANY question, STOP and fix it before proceeding.

---

## 📊 File Structure Map

```
minecraft-mod/
├── src/main/
│   ├── java/com/example/educationmod/
│   │   ├── core/           # Data layer (RelationalStore, KnowledgeGapTracker)
│   │   ├── debug/          # Developer tools (Console, profiling)
│   │   ├── gui/            # UI screens (Settings, HUD, Menus)
│   │   ├── layers/         # Layered Learning system
│   │   ├── *.java          # Top-level managers (ChatQuizHandler, etc)
│   │   └── ModMenuIntegration.java  # Mod Menu API
│   └── resources/
│       ├── fabric.mod.json         # Mod metadata + entrypoints
│       └── assets/educationmod/
│           └── default_content/    # Bundled sample content
│               ├── topics/         # Sample quiz topics
│               ├── events/         # Sample triggers
│               └── courses/        # Sample curriculum
├── build/libs/                     # Build output (ONLY latest JAR)
├── scripts/                        # Deployment automation
│   ├── pre-push-check.bat         # Consistency validation
│   └── upgrade-mod.bat            # User upgrade script
├── .gemini/antigravity/brain/.../  # Development artifacts
│   ├── task.md                    # Task tracking
│   ├── implementation_plan.md     # Current plan
│   ├── walkthrough.md             # What was built
│   ├── handoff.md                 # Architecture reference
│   ├── strategic_options.md       # Long-term roadmap
│   └── DEVELOPER_ONBOARDING.md    # This file
├── README.md                      # User guide
├── gradle.properties              # Version source of truth
└── .gitignore                     # Keeps GitHub clean
```

---

## 🎯 Understanding the Version Strategy

### Version Format: `vXXX`
- `v021`: Recovery & Stability (Console, Advanced Querying)
- `v022`: Standardization (Mod Menu Integration)
- `v023`: Adaptive Learning Engine (Text Input, Gap Tracking)
- `v024`: Final Polish (HUD Fix, Documentation)

### When to Increment Version
1. **Major Feature**: New learning capability (increment by 1)
2. **Minor Feature**: UI improvement, new command (increment by 1)
3. **Bug Fix**: Only if releasing to users (increment by 1)
4. **Documentation Only**: Do NOT increment version

### How to Release a New Version
```powershell
# 1. Update version in gradle.properties
# 2. Add entry to task.md
# 3. Build
.\gradlew.bat build

# 4. Delete old JARs
Remove-Item build/libs/*v0XX*.jar  # (keep only new version)

# 5. Run pre-push check
.\scripts\pre-push-check.bat

# 6. Commit to GitHub
git add .
git commit -m "Release vXXX: [Feature Name]"
git push origin main
```

---

## 🔧 Common Tasks

### Adding a New Feature
1. Read current `task.md` to understand what's in progress
2. Add task to appropriate version section in `task.md`
3. Update `implementation_plan.md` with technical design
4. Implement feature in appropriate package (`core/`, `gui/`, `debug/`)
5. Test using Developer Console
6. Document in `walkthrough.md`
7. Run pre-push check

### Fixing a Bug
1. Reproduce the bug manually
2. Check `DeveloperConsole` logs for clues
3. Add test case to verification plan
4. Fix bug
5. Verify fix manually
6. Document in `walkthrough.md` under current version
7. Do NOT increment version unless releasing to users

### Adding Sample Content
1. Create JSON file in `src/main/resources/assets/educationmod/default_content/topics/`
2. Follow existing JSON schema (see `welcome_basics.json`)
3. Validate JSON: https://jsonlint.com
4. Rebuild to bundle: `.\gradlew.bat build`
5. Test by loading mod in-game

---

## 🚨 Red Flags (Stop If You See These)

1. **Multiple version JARs** in `build/libs/` → Clean old ones
2. **Completed task not in walkthrough.md** → Document it
3. **Version mismatch** between `gradle.properties` and documentation → Fix it
4. **Build warnings about Mod Menu** → Dependency issue
5. **Corrupted implementation_plan.md** (duplicate content) → Rewrite cleanly
6. **Creating new files without updating architecture docs** → Update handoff.md

---

## 📚 Key Design Patterns

### Pattern 1: Settings Management
```java
// ALWAYS use ModSettings for persistent state
ModSettings.getHudX();
ModSettings.setHudX(newX);
// This auto-saves to config/mod_data/mod_settings.json
```

### Pattern 2: Console Logging
```java
// Use for debugging
EducationMod.LOGGER.info("Feature initialized");

// Use for user feedback
DeveloperConsole.getInstance().log("Quiz loaded", 0x00FF00);
```

### Pattern 3: Singleton Access
```java
// Most managers use getInstance()
RelationalStore.getInstance().getTopics();
KnowledgeGapTracker.getInstance().logGap(...);
PlayerStats.getInstance().recordResult(...);
```

---

## 🎓 Learning Resources

### Understanding the Codebase
1. **Start Here**: `EducationMod.java` and `EducationModClient.java` (entry points)
2. **Data Flow**: `ModConfigManager` → `RelationalStore` → `ChatQuizHandler`
3. **UI Flow**: Keybind → `ModMenuScreen` → `SettingsScreen`
4. **Debug Flow**: `DeveloperConsole` → `RelationalStore` queries

### External References
- Fabric API Docs: https://fabricmc.net/develop/
- Mod Menu API: https://github.com/TerraformersMC/ModMenu
- Minecraft 1.21.5 Mappings: Check gradle.properties

---

## ✅ Success Criteria

You've successfully onboarded when you can:
1. Build the mod from scratch: `.\gradlew.bat build`
2. Explain the difference between `RelationalStore` and `KnowledgeGapTracker`
3. Add a new quiz topic without touching Java code
4. Open the Developer Console in-game and run `study` command
5. Identify which version is current by reading `gradle.properties`
6. Run pre-push checks without errors

---

## 🔮 Future Developer Notes

**What NOT to Do:**
- Don't hardcode quiz content in Java (use JSON)
- Don't create new settings classes (use ModSettings)
- Don't skip pre-push checks (it saves headaches)
- Don't commit multiple version JARs (`.gitignore` handles this)

**What TO Do:**
- Always read `task.md` before starting work
- Always update documentation when changing architecture
- Always test features manually (use Developer Console)
- Always run `.\scripts\pre-push-check.bat` before committing

---

**Remember**: This document exists so you never have to ask "What should I do?" or "Did I miss something?" The answer is always in this guide. Read it, follow it, and the codebase will stay healthy.

**Last Updated**: v024 (2025-11-28)
