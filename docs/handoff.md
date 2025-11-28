# Handoff Document - v024

**Current Version**: v024
**Strategy**: Adaptive Learning Engine (Complete)
**Status**: Production Ready

## System State
The **Adaptive Learning Engine** with Mod Menu integration is fully operational. The mod learns from user mistakes and generates personalized study materials.

## v024 Key Features
1. **Standardization**: Mod Menu integration for settings (community standard)
2. **Intelligence**: Text-based quiz answers with full sentence support
3. **Learning**: Knowledge gap tracking and dynamic study set generation
4. **Debugging**: Developer Console overlay with advanced querying
5. **Deployment**: Automated upgrade scripts and pre-push validation

## Development Philosophy & "Rules of Engagement"

### Core Principles
1. **Compression Over Replacement**: Each developer refines and improves the existing code/docs. Never start from scratch - build on what works.
2. **Data-Driven First**: The mod is an engine, not hardcoded content. All quizzes, triggers, and courses live in JSON.
3. **Documentation is Code**: The `docs/` folder is the source of truth. Update it before coding, not after.
4. **Self-Documenting**: Read `DEVELOPER_ONBOARDING.md` before starting. It contains the entire workflow and self-check protocol.

### The Three Pillars
1. **Relational Store** (`core/RelationalStore.java`) - In-memory database with integrity enforcement
2. **Developer Console** (`debug/DeveloperConsole.java`) - Real-time debugging with overlay mode
3. **Adaptive Learning** (`core/KnowledgeGapTracker.java`) - Tracks mistakes and generates study sets

## Architecture Map
```
Core Layer:
├── RelationalStore      # Data integrity
├── KnowledgeGapTracker  # Learning analytics
└── ModConfigManager     # JSON loading

Frontend Layer:
├── ModMenuIntegration   # Standard settings access
├── LearningHUD          # Top-left overlay
└── DeveloperConsole     # Debug overlay (~ key)

Logic Layer:
├── ChatQuizHandler      # Text-based quizzes
├── TriggerRegistry      # Event → Quiz mapping
└── LayerManager         # Concept stacking
```

## Workflow for Next Developer
1. Read `docs/DEVELOPER_ONBOARDING.md` (comprehensive guide)
2. Run `.\scripts\pre-push-check.bat` to verify consistency
3. Review `docs/task.md` to see what's next
4. Create `implementation_plan.md` for new work
5. Code → Test → Document → Compress back to `docs/`

## Final Note
This project grows iteratively. Each session refines and compresses improvements back into the canonical `docs/` folder. The engine is stable - focus on content and user experience next.

**Last Updated**: v024 (2025-11-28)
