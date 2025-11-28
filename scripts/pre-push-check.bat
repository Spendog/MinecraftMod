@echo off
REM Pre-Push Consistency Check
REM Run this before every GitHub push to ensure quality

echo ========================================
echo   Education Mod - Pre-Push Check
echo ========================================
echo.

set ERROR_COUNT=0

REM Check 1: Version consistency
echo [1/6] Checking version consistency...
findstr /C:"mod_version = v024" gradle.properties >nul
if %ERRORLEVEL% NEQ 0 (
    echo [FAIL] gradle.properties version mismatch!
    set /a ERROR_COUNT+=1
) else (
    echo [PASS] gradle.properties version is v024
)

REM Check 2: Only one version JAR exists
echo [2/6] Checking build artifacts...
set JAR_COUNT=0
for %%f in (build\libs\MinecraftEDU_v1.21.5-v*.jar) do set /a JAR_COUNT+=1
if %JAR_COUNT% GTR 2 (
    echo [FAIL] Multiple version JARs found! Clean old builds.
    set /a ERROR_COUNT+=1
) else (
    echo [PASS] Only v024 JAR present
)

REM Check 3: README mentions current version
echo [3/6] Checking README.md...
findstr /C:"v024" README.md >nul
if %ERRORLEVEL% NEQ 0 (
    echo [FAIL] README.md does not mention v024!
    set /a ERROR_COUNT+=1
) else (
    echo [PASS] README.md references v024
)

REM Check 4: handoff.md is updated
echo [4/6] Checking handoff.md...
findstr /C:"v024" C:\Users\minin\.gemini\antigravity\brain\15ecde54-cf55-46e5-9163-da55811575b8\handoff.md >nul
if %ERRORLEVEL% NEQ 0 (
    echo [FAIL] handoff.md version not updated!
    set /a ERROR_COUNT+=1
) else (
    echo [PASS] handoff.md is current
)

REM Check 5: Build succeeds
echo [5/6] Running clean build...
call gradlew.bat build --quiet
if %ERRORLEVEL% NEQ 0 (
    echo [FAIL] Build failed!
    set /a ERROR_COUNT+=1
) else (
    echo [PASS] Build successful
)

REM Check 6: Sample content exists
echo [6/6] Checking sample content...
if exist "src\main\resources\assets\educationmod\default_content\topics\welcome_basics.json" (
    echo [PASS] Sample content present
) else (
    echo [FAIL] Sample content missing!
    set /a ERROR_COUNT+=1
)

echo.
echo ========================================
if %ERROR_COUNT% EQU 0 (
    echo   ALL CHECKS PASSED
    echo   Ready to push to GitHub!
    exit /b 0
) else (
    echo   %ERROR_COUNT% CHECKS FAILED
    echo   Fix errors before pushing!
    exit /b 1
)
