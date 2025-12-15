@echo off
echo ============================================
echo MindCare Admin Features - Rebuild Script
echo ============================================
echo.
echo This script will clean and rebuild your project
echo to resolve compilation errors.
echo.
pause

cd /d "%~dp0"

echo.
echo Step 1: Cleaning project...
call gradlew.bat clean

echo.
echo Step 2: Building project...
call gradlew.bat build

echo.
echo ============================================
echo Build Complete!
echo ============================================
echo.
echo If build was successful, you can now:
echo 1. Open Android Studio
echo 2. File ^> Sync Project with Gradle Files
echo 3. Run the app
echo.
echo If errors persist:
echo 1. In Android Studio: File ^> Invalidate Caches / Restart
echo 2. Delete the 'app/build' folder manually
echo 3. Run this script again
echo.
pause

