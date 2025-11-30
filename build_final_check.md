
> Configure project :
Fabric Loom: 1.10.5

> Task :clean

> Task :compileJava FAILED
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:13: error: class, interface, enum, or record expected
import net.minecraft.item.ItemStack;this.requirementData=itemId;this.requirementIconOverride=stack;this.selectedSlot=2; // Focus
                                    ^
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:13: error: class, interface, enum, or record expected
import net.minecraft.item.ItemStack;this.requirementData=itemId;this.requirementIconOverride=stack;this.selectedSlot=2; // Focus
                                                                ^
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:13: error: class, interface, enum, or record expected
import net.minecraft.item.ItemStack;this.requirementData=itemId;this.requirementIconOverride=stack;this.selectedSlot=2; // Focus
                                                                                                   ^
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:16: error: class, interface, enum, or record expected
com.example.educationmod.ActivityLogger.log("Requirement set to Holding: "+itemId);}
^
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:16: error: class, interface, enum, or record expected
com.example.educationmod.ActivityLogger.log("Requirement set to Holding: "+itemId);}
                                                                                   ^
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:18: error: unnamed classes are a preview feature and are disabled by default.
@Override public boolean shouldPause(){return false;}
                 ^
  (use --enable-preview to enable unnamed classes)
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:145: error: class, interface, enum, or record expected
if(!icon.isEmpty()){context.drawItem(icon,x+size/2-8,y+size/2-8);context.drawCenteredTextWithShadow(this.textRenderer,label,x+size/2,y+size+5,0xFFFFFF);}else{context.drawCenteredTextWithShadow(this.textRenderer,label,x+size/2,y+size/2-4,0xAAAAAA);}}}
                                                                                                                                                                                                                                                         ^
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:1: error: unnamed class should not have package declaration
package com.example.educationmod.gui;
^
8 errors

[Incubating] Problems report is available at: file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileJava'.
> Compilation failed; see the compiler output below.
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:1: error: unnamed class should not have package declaration
  package com.example.educationmod.gui;
  ^C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:18: error: unnamed classes are a preview feature and are disabled by default.
  @Override public boolean shouldPause(){return false;}
                   ^
    (use --enable-preview to enable unnamed classes)C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:13: error: class, interface, enum, or record expected
  import net.minecraft.item.ItemStack;this.requirementData=itemId;this.requirementIconOverride=stack;this.selectedSlot=2; // Focus
                                      ^
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:13: error: class, interface, enum, or record expected
  import net.minecraft.item.ItemStack;this.requirementData=itemId;this.requirementIconOverride=stack;this.selectedSlot=2; // Focus
                                                                  ^
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:13: error: class, interface, enum, or record expected
  import net.minecraft.item.ItemStack;this.requirementData=itemId;this.requirementIconOverride=stack;this.selectedSlot=2; // Focus
                                                                                                     ^
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:16: error: class, interface, enum, or record expected
  com.example.educationmod.ActivityLogger.log("Requirement set to Holding: "+itemId);}
  ^
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:16: error: class, interface, enum, or record expected
  com.example.educationmod.ActivityLogger.log("Requirement set to Holding: "+itemId);}
                                                                                     ^
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\TriggerDashboardScreen.java:145: error: class, interface, enum, or record expected
  if(!icon.isEmpty()){context.drawItem(icon,x+size/2-8,y+size/2-8);context.drawCenteredTextWithShadow(this.textRenderer,label,x+size/2,y+size+5,0xFFFFFF);}else{context.drawCenteredTextWithShadow(this.textRenderer,label,x+size/2,y+size/2-4,0xAAAAAA);}}}
                                                                                                                                                                                                                                                           ^
  8 errors

* Try:
> Check your code and dependencies to fix the compilation error(s)
> Run with --scan to get full insights.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.12/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 1s
2 actionable tasks: 2 executed
