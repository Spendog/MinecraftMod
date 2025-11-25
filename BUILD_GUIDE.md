# Build and Run Guide

This guide explains how to build the **Education Mod** (Fabric 1.21.5) and install it in your Minecraft client.

## Prerequisites
- **Java 21** (Required for Minecraft 1.21+)
- **Minecraft Launcher**

## Building the Mod
1.  Open a terminal in the project root directory.
2.  Run the build command:
    - **Windows**: `.\gradlew build`
    - **Mac/Linux**: `./gradlew build`
3.  Wait for the build to complete. The first time will take a while to download dependencies.

## Locating the Mod File
Once the build is successful, your mod file will be located at:
`build/libs/educationmod-1.0.0.jar`

> [!NOTE]
> You might see other files like `educationmod-1.0.0-sources.jar` or `educationmod-1.0.0-dev.jar`. You want the main one without suffixes (or just the version number).

## Installing the Mod
1.  Make sure you have **Fabric Loader** installed for Minecraft 1.21.1 (or the version specified in `gradle.properties`).
2.  Navigate to your Minecraft installation folder:
    - **Windows**: `%appdata%\.minecraft`
3.  Open the `mods` folder.
4.  Copy the `educationmod-1.0.0.jar` file into the `mods` folder.
5.  Launch Minecraft using the Fabric profile.

## Configuration
The mod creates a configuration folder at `.minecraft/config/mod_data/`.
- **`topics/`**: Place your Quiz JSON files here.
- **`events/`**: Place your Event JSON files here.

### Example Event (`events/example.json`)
```json
{
  "trigger": "BLOCK_BREAK",
  "condition": "block.minecraft.diamond_block",
  "action": {
    "type": "QUIZ",
    "data": "math.json"
  }
}
```

### Example Quiz (`topics/math.json`)
```json
{
  "questions": [
    {
      "question": "What is 2+2?",
      "correct_answer": "4",
      "incorrect_answers": ["3", "5"]
    }
  ]
}
```
