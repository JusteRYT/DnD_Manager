# --- НАСТРОЙКИ ---
$APP_NAME = "DnD_Manager"
$APP_VERSION = "1.0-SNAPSHOT"
$ICON_PATH = "src/main/resources/com/example/dnd_manager/icon/App_icon.ico"
$JAVAFX_SDK_WIN = "C:\Users\JusteRYT\.jdks\openjfx-23.0.2_windows-x64_bin-sdk\javafx-sdk-23.0.2"

# Поиск JDK
$JAVA_HOME_WIN = $env:JAVA_HOME
if (-not $JAVA_HOME_WIN -or -not (Test-Path "$JAVA_HOME_WIN\bin\jlink.exe")) {
    $foundJdk = Get-ChildItem "C:\Program Files\Java\" | Where-Object { $_.Name -like "jdk*" } | Sort-Object Name -Descending | Select-Object -First 1
    if ($foundJdk) { $JAVA_HOME_WIN = $foundJdk.FullName }
}

# 0. Очистка
Write-Host "--- Cleaning up ---" -ForegroundColor Yellow
Stop-Process -Name "javaw" -Force -ErrorAction SilentlyContinue
if (Test-Path "dist") { Remove-Item -Recurse -Force "dist" }

# 1. Сборка Maven
Write-Host "--- Building JAR with Maven ---" -ForegroundColor Cyan
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) { Write-Error "Maven build failed"; exit }

# Подготовка входной папки
$INPUT_DIR = "target\jpackage-input"
if (Test-Path $INPUT_DIR) { Remove-Item -Recurse -Force $INPUT_DIR }
New-Item -ItemType Directory -Path $INPUT_DIR
Copy-Item "target\$APP_NAME-$APP_VERSION.jar" -Destination "$INPUT_DIR\app.jar"

# 2. Создание EXE образа
Write-Host "--- Creating EXE with jpackage ---" -ForegroundColor Cyan
& "$JAVA_HOME_WIN\bin\jpackage.exe" `
  --type app-image `
  --dest dist `
  --name $APP_NAME `
  --input $INPUT_DIR `
  --main-jar "app.jar" `
  --main-class com.example.dnd_manager.Launcher `
  --module-path "$JAVAFX_SDK_WIN\lib" `
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,java.base,java.desktop,jdk.unsupported `
  --icon $ICON_PATH `
  --vendor "JusteRYT" `
  --description "D&D Character Manager"

if ($LASTEXITCODE -ne 0) { Write-Error "jpackage failed"; exit }

# --- ШАГ 2.1: ИСПРАВЛЕНИЕ ГРАФИКИ (Копируем DLL) ---
Write-Host "--- Fixing Graphics Pipeline (copying DLLs) ---" -ForegroundColor Yellow
# Копируем все DLL из папки bin JavaFX SDK прямо в bin рантайма приложения
$RUNTIME_BIN = "dist\$APP_NAME\runtime\bin"
if (Test-Path "$JAVAFX_SDK_WIN\bin") {
    Copy-Item "$JAVAFX_SDK_WIN\bin\*.dll" -Destination $RUNTIME_BIN -Force
} else {
    Write-Warning "JavaFX bin folder not found! Check your SDK path."
}

# --- ШАГ 2.2: СОЗДАНИЕ DEBUG-ЗАПУСКАТЕЛЯ ---
Write-Host "--- Creating Debug Script ---" -ForegroundColor Cyan

$debugBat = @"
@echo off
setlocal
cd /d "%~dp0"
echo Starting DnD_Manager with Log redirection...
echo Output will be saved to: debug_output.txt

REM Запускаем основной EXE и перенаправляем стандартный вывод и ошибки в файл
DnD_Manager.exe > debug_output.txt 2>&1

if %ERRORLEVEL% neq 0 (
    echo.
    echo Application exited with code %ERRORLEVEL%
    echo Check debug_output.txt for details.
    pause
)
endlocal
"@

[System.IO.File]::WriteAllLines("$PSScriptRoot\dist\$APP_NAME\debug_launcher.bat", $debugBat)

# 3. Архивация
Write-Host "--- Zipping for GitHub Release ---" -ForegroundColor Cyan
$ZIP_PATH = "target\${APP_NAME}_Windows.zip"
if (Test-Path $ZIP_PATH) { Remove-Item $ZIP_PATH }
Compress-Archive -Path "dist\$APP_NAME\*" -DestinationPath $ZIP_PATH

Write-Host "--- Done! ---" -ForegroundColor Green
Write-Host "Archive for GitHub: $ZIP_PATH"