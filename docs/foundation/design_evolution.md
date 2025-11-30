# Design Evolution & Feature Hall of Fame

> **Philosophy**: Code is ephemeral; Ideas are persistent. We are willing to break a stable feature to reintroduce it in a more perfect form. The JSON data is the soul; the code is just the body.

## 🧬 The Evolution Cycle
**Concept** → **Implementation** → **Break & Learn** → **Reintroduce (Perfected)**

---

## 🏆 Feature: The Developer Console
### v020 Implementation (The "Good")
- **What was Great**: It existed. It allowed us to see logs without crashing the game.
- **The Flaw**: It was a separate screen. You couldn't see the game *while* debugging.
- **The Repair**: We "broke" the screen logic and reintroduced it as an **Overlay**.
### v024 Implementation (The "Perfected")
- **Current State**: A Quake-style overlay (`~` key) that renders *on top* of gameplay.
- **Why it's better**: You can see the trigger fire *as* you break the block.
- **Future Vision**: A fully interactive REPL where you can modify game state live.

---

## 🏆 Feature: The Learning HUD
### v012 Implementation (The "Good")
- **What was Great**: Constant visual feedback. "Passive Learning" via a ticker.
- **The Flaw**: It was hardcoded. It overlapped with other mods. It disappeared randomly.
- **The Repair**: We added `ModSettings` to control position/opacity and added safety checks (v024) to reset coordinates.
### v024 Implementation (The "Perfected")
- **Current State**: A customizable, crash-proof overlay.
- **Why it's better**: It respects the user's screen space and "heals" itself if coordinates break.
- **Future Vision**: A dynamic "Iron Man" HUD that highlights in-world objects with learning tags.

---

## 🏆 Feature: The Quiz Engine
### v002 Implementation (The "Good")
- **What was Great**: It asked questions. It worked.
- **The Flaw**: Multiple choice only. Click-based. Felt like a "test", not a conversation.
- **The Repair**: We "broke" the input handler in v023 to accept **Text Input**.
### v024 Implementation (The "Perfected")
- **Current State**: A Natural Language Processor. You type `/edu Photosynthesis`.
- **Why it's better**: It forces recall, not just recognition. It feels like chatting with a tutor.
- **Future Vision**: AI-driven analysis of *why* the answer was wrong (Semantic matching).

---

## 🛡️ The "Safe Core" (JSON Data)
**Why we can be aggressive**:
All educational content lives in `config/mod_data/`.
- `topics/`: The questions.
- `events/`: The triggers.
- `courses/`: The curriculum.

**Rule**: We can burn the code to the ground and rebuild it, as long as the new engine can still read these JSON files. The *knowledge* is safe.

---

## 🔮 Wishlist (Features to Break & Rebuild)
1.  **The Settings Screen**: Currently a manual list of buttons. **Goal**: Break it. Rebuild as a data-driven UI generated from a JSON schema.
2.  **The Trigger Editor**: Currently functional but ugly. **Goal**: Break it. Rebuild as a visual node graph (like Unreal Blueprints).
