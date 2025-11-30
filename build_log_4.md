
> Configure project :
Fabric Loom: 1.10.5

> Task :compileJava FAILED
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:44: error: cannot find symbol
            public DraggableStackVisitor.DraggedAcceptorResult acceptDraggedStack(
                                        ^
  symbol:   class DraggedAcceptorResult
  location: interface DraggableStackVisitor
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:40: error: incompatible types: bad return type in lambda expression
                return Stream.of((DraggableStackVisitor.BoundsProvider) () -> new Rectangle(x, y, slotSize, slotSize));
                                                                              ^
    Rectangle cannot be converted to VoxelShape
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:55: error: cannot find symbol
                    return DraggableStackVisitor.DraggedAcceptorResult.CONSUMED;
                                                ^
  symbol:   variable DraggedAcceptorResult
  location: interface DraggableStackVisitor
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:57: error: cannot find symbol
                return DraggableStackVisitor.DraggedAcceptorResult.PASS;
                                            ^
  symbol:   variable DraggedAcceptorResult
  location: interface DraggableStackVisitor
Note: C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\DynamicSettingsScreen.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
4 errors

[Incubating] Problems report is available at: file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileJava'.
> Compilation failed; see the compiler output below.
  Note: Recompile with -Xlint:unchecked for details.C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:44: error: cannot find symbol
              public DraggableStackVisitor.DraggedAcceptorResult acceptDraggedStack(
                                          ^
    symbol:   class DraggedAcceptorResult
    location: interface DraggableStackVisitor
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:55: error: cannot find symbol
                      return DraggableStackVisitor.DraggedAcceptorResult.CONSUMED;
                                                  ^
    symbol:   variable DraggedAcceptorResult
    location: interface DraggableStackVisitor
  C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:57: error: cannot find symbol
                  return DraggableStackVisitor.DraggedAcceptorResult.PASS;
                                              ^
    symbol:   variable DraggedAcceptorResult
    location: interface DraggableStackVisitorNote: C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\DynamicSettingsScreen.java uses unchecked or unsafe operations.C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:40: error: incompatible types: bad return type in lambda expression
                  return Stream.of((DraggableStackVisitor.BoundsProvider) () -> new Rectangle(x, y, slotSize, slotSize));
                                                                                ^
      Rectangle cannot be converted to VoxelShape
  4 errors

* Try:
> Check your code and dependencies to fix the compilation error(s)
> Run with --scan to get full insights.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.12/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 2s
1 actionable task: 1 executed
