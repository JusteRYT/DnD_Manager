# --- НАСТРОЙКИ ---
$APP_NAME = "DnD_Manager"
$APP_VERSION = "1.0-SNAPSHOT"

# Папка для сборки (вне target, чтобы избежать конфликтов доступа)
$BUILD_ROOT = "dist_build"
$STAGING_DIR = "$BUILD_ROOT\$APP_NAME" # Та самая папка, которая будет внутри архива
$ZIP_PATH = "target\${APP_NAME}_Windows.zip"

# 0. Убиваем процессы и чистим старое
Write-Host "--- Cleaning up processes and old builds ---" -ForegroundColor Yellow
Stop-Process -Name "javaw" -Force -ErrorAction SilentlyContinue
Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue

if (Test-Path $BUILD_ROOT) {
    # Небольшая пауза, чтобы Windows "отпустил" файлы после убийства процесса
    Start-Sleep -Milliseconds 500
    Remove-Item -Recurse -Force $BUILD_ROOT -ErrorAction SilentlyContinue
}

# Авто-поиск JDK
$JAVA_HOME_WIN = $env:JAVA_HOME
if (-not $JAVA_HOME_WIN -or -not (Test-Path "$JAVA_HOME_WIN\bin\jlink.exe")) {
    $foundJdk = Get-ChildItem "C:\Program Files\Java\" | Where-Object { $_.Name -like "jdk*" } | Sort-Object Name -Descending | Select-Object -First 1
    if ($foundJdk) { $JAVA_HOME_WIN = $foundJdk.FullName }
}

$JAVAFX_SDK_WIN = "C:\Users\JusteRYT\.jdks\openjfx-23.0.2_windows-x64_bin-sdk\javafx-sdk-23.0.2"

# 1. Сборка Maven
Write-Host "--- Building JAR with Maven ---" -ForegroundColor Cyan
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) { Write-Error "Maven build failed"; exit }

# 2. Подготовка структуры папок
Write-Host "--- Preparing structure ---" -ForegroundColor Cyan
New-Item -ItemType Directory -Force -Path "$STAGING_DIR\app"

# 3. Создание Runtime (jlink)
Write-Host "--- Creating Custom Runtime ---" -ForegroundColor Cyan
& "$JAVA_HOME_WIN\bin\jlink.exe" `
  --module-path "$JAVA_HOME_WIN\jmods;$JAVAFX_SDK_WIN\lib" `
  --add-modules java.base,java.desktop,jdk.unsupported,javafx.controls,javafx.fxml,javafx.graphics `
  --output "$STAGING_DIR\runtime" `
  --strip-debug `
  --compress 2 `
  --no-header-files `
  --no-man-pages

if ($LASTEXITCODE -ne 0) { Write-Error "jlink failed"; exit }

# 4. Копирование файлов
Write-Host "--- Copying App and DLLs ---" -ForegroundColor Cyan
Copy-Item "target\$APP_NAME-$APP_VERSION.jar" -Destination "$STAGING_DIR\app\app.jar"
Copy-Item "$JAVAFX_SDK_WIN\bin\*.dll" -Destination "$STAGING_DIR\runtime\bin\"

# 5. Создание батника запуска (теперь он лежит в корне папки DnD_Manager)
Write-Host "--- Creating launch script ---" -ForegroundColor Cyan
$batContent = @"
@echo off
set DIR=%~dp0
start "" /b "%DIR%runtime\bin\javaw.exe" -Dprism.verbose=true -jar "%DIR%app\app.jar"
exit
"@
[System.IO.File]::WriteAllLines("$STAGING_DIR\run.bat", $batContent)

# 6. Архивация
Write-Host "--- Zipping ---" -ForegroundColor Cyan
if (Test-Path $ZIP_PATH) { Remove-Item $ZIP_PATH }
# Архивируем папку DnD_Manager целиком, чтобы она была корневой в архиве
Compress-Archive -Path $STAGING_DIR -DestinationPath $ZIP_PATH

Write-Host "--- Done! ---" -ForegroundColor Green
Write-Host "Archive is at: $ZIP_PATH"