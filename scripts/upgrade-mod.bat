@echo off
REM Education Mod - Automatic Upgrade Script
REM This script safely upgrades the mod while preserving your learning progress

echo ========================================
echo   Education Mod - Upgrade to v024
echo ========================================
echo.

REM Define paths
set MOD_FOLDER=%APPDATA%\.minecraft\mods
set CONFIG_FOLDER=%APPDATA%\.minecraft\config\mod_data
set BACKUP_FOLDER=%APPDATA%\.minecraft\mod_backups\%date:~-4,4%%date:~-10,2%%date:~-7,2%_%time:~0,2%%time:~3,2%%time:~6,2%

echo [1/5] Backing up your data...
if not exist "%BACKUP_FOLDER%" mkdir "%BACKUP_FOLDER%"
if exist "%CONFIG_FOLDER%" (
    xcopy "%CONFIG_FOLDER%\*" "%BACKUP_FOLDER%\config\" /E /I /Y >nul
    echo   ✓ Backed up config to %BACKUP_FOLDER%
) else (
    echo   ℹ No existing config found (fresh install)
)

echo [2/5] Removing old mod versions...
if exist "%MOD_FOLDER%\MinecraftEDU_v1.21.5-v0*.jar" (
    del "%MOD_FOLDER%\MinecraftEDU_v1.21.5-v0*.jar"
    echo   ✓ Removed old versions
) else (
    echo   ℹ No old versions found
)

echo [3/5] Installing v024...
copy "build\libs\MinecraftEDU_v1.21.5-v024.jar" "%MOD_FOLDER%\" >nul
if %ERRORLEVEL% EQU 0 (
    echo   ✓ Installed v024 to mods folder
) else (
    echo   ✗ Failed to install! Check permissions.
    pause
    exit /b 1
)

echo [4/5] Restoring your learning progress...
if exist "%BACKUP_FOLDER%\config\mod_settings.json" (
    copy "%BACKUP_FOLDER%\config\mod_settings.json" "%CONFIG_FOLDER%\" >nul
    echo   ✓ Restored your settings
)
if exist "%BACKUP_FOLDER%\config\player_stats.json" (
    copy "%BACKUP_FOLDER%\config\player_stats.json" "%CONFIG_FOLDER%\" >nul
    echo   ✓ Restored your progress
)

echo [5/5] Installing sample content...
if not exist "%CONFIG_FOLDER%\topics" mkdir "%CONFIG_FOLDER%\topics"
if not exist "%CONFIG_FOLDER%\topics\welcome_basics.json" (
    REM Extract sample content from JAR (if bundled)
    echo   ℹ Sample content will be created on first launch
)

echo.
echo ========================================
echo   ✓ UPGRADE COMPLETE
echo ========================================
echo.
echo Your learning progress has been preserved.
echo Start Minecraft and enjoy the new features!
echo.
echo New in v024:
echo   • Adaptive Learning Engine
echo   • Text-based quiz answers
echo   • Knowledge gap tracking
echo   • Study command for review
echo.
pause
