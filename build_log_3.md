
> Configure project :
Fabric Loom: 1.10.5

> Task :compileJava FAILED
C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:42: error: cannot find symbol
                return Stream.of(BoundsProvider.of(new Rectangle(x, y, slotSize, slotSize)));
                                               ^
  symbol:   method of(Rectangle)
  location: interface BoundsProvider
Note: C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\DynamicSettingsScreen.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
1 error

[Incubating] Problems report is available at: file:///C:/Users/minin/.gemini/antigravity/scratch/minecraft-mod/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileJava'.
> Compilation failed; see the compiler output below.
  Note: Recompile with -Xlint:unchecked for details.Note: C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\gui\DynamicSettingsScreen.java uses unchecked or unsafe operations.C:\Users\minin\.gemini\antigravity\scratch\minecraft-mod\src\main\java\com\example\educationmod\rei\EducationREIPlugin.java:42: error: cannot find symbol
                  return Stream.of(BoundsProvider.of(new Rectangle(x, y, slotSize, slotSize)));
                                                 ^
    symbol:   method of(Rectangle)
    location: interface BoundsProvider
  1 error

* Try:
> Check your code and dependencies to fix the compilation error(s)
> Run with --scan to get full insights.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.12/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 2s
1 actionable task: 1 executed
