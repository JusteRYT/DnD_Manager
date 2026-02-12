# --- НАСТРОЙКИ ---
$APP_NAME = "DnD_Manager"
$APP_VERSION = "1.0-SNAPSHOT"
# Укажи свои пути к JDK и SDK на Windows
$JAVA_HOME_WIN = "C:\Program Files\Java\jdk-21"
$JAVAFX_SDK_WIN = "C:\javafx-sdk-21" # Путь к распакованному JavaFX SDK
$TARGET_DIR = "target\dist_windows"

# 1. Сборка Maven
Write-Host "--- Building JAR with Maven ---" -ForegroundColor Cyan
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) { Write-Error "Maven build failed"; exit }

# 2. Очистка старых сборок
Write-Host "--- Preparing directories ---" -ForegroundColor Cyan
if (Test-Path $TARGET_DIR) { Remove-Item -Recurse -Force $TARGET_DIR }
if (Test-Path "target\runtime") { Remove-Item -Recurse -Force "target\runtime" }
New-Item -ItemType Directory -Force -Path "$TARGET_DIR\app"

# 3. Создание Runtime (jlink)
Write-Host "--- Creating Custom Runtime ---" -ForegroundColor Cyan
& "$JAVA_HOME_WIN\bin\jlink.exe" `
  --module-path "$JAVA_HOME_WIN\jmods;$JAVAFX_SDK_WIN\lib" `
  --add-modules java.base,java.desktop,jdk.unsupported,javafx.controls,javafx.fxml,javafx.graphics `
  --output "$TARGET_DIR\runtime" `
  --compress 2 `
  --no-header-files `
  --no-man-pages

if ($LASTEXITCODE -ne 0) { Write-Error "jlink failed"; exit }

# 4. Копирование JAR и нативных библиотек (.dll)
Write-Host "--- Copying App and Native Libs (.dll) ---" -ForegroundColor Cyan
Copy-Item "target\$APP_NAME-$APP_VERSION.jar" -Destination "$TARGET_DIR\app\app.jar"

# КРИТИЧЕСКИЙ ШАГ ДЛЯ WINDOWS:
# Копируем DLL из bin папки SDK в bin папку нашего рантайма
Copy-Item "$JAVAFX_SDK_WIN\bin\*.dll" -Destination "$TARGET_DIR\runtime\bin\"

# 5. Создание батника запуска (run.bat)
Write-Host "--- Creating launch script ---" -ForegroundColor Cyan
$batContent = @"
@echo off
set DIR=%~dp0
"%DIR%runtime\bin\java.exe" -Dprism.verbose=true -jar "%DIR%app\app.jar"
pause
"@
# Используем UTF8 без BOM для совместимости с Windows CMD
[System.IO.File]::WriteAllLines("$TARGET_DIR\run.bat", $batContent)

# 6. Архивация
Write-Host "--- Zipping ---" -ForegroundColor Cyan
if (Test-Path "target\${APP_NAME}_Windows.zip") { Remove-Item "target\${APP_NAME}_Windows.zip" }
Compress-Archive -Path "$TARGET_DIR\*" -DestinationPath "target\${APP_NAME}_Windows.zip"

Write-Host "--- Done! ---" -ForegroundColor Green
Write-Host "Archive is at: target\${APP_NAME}_Windows.zip"