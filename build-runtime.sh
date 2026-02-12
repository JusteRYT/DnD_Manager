#!/bin/bash

# --- НАСТРОЙКИ ---
APP_NAME="DnD_Manager"
APP_VERSION="1.0-SNAPSHOT"
JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
JAVAFX_SDK="/home/sergey/openjfx-17.0.18_linux-x64_bin-sdk/javafx-sdk-17.0.18"

# Новая структура путей
RELEASE_NAME="${APP_NAME}_Linux"
# Путь, где будет лежать само приложение
FINAL_APP_DIR="target/$RELEASE_NAME/$APP_NAME"

# 1. Очистка и сборка JAR
echo "--- Building JAR with Maven ---"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then echo "Maven build failed"; exit 1; fi

# 2. Подготовка папок
echo "--- Preparing directories ---"
# Удаляем старые сборки, чтобы не было мусора
rm -rf "target/$RELEASE_NAME"
mkdir -p "$FINAL_APP_DIR/app"

# 3. Создание Runtime (Java + JavaFX)
echo "--- Creating Custom Runtime ---"
$JAVA_HOME/bin/jlink \
  --module-path "$JAVA_HOME/jmods:$JAVAFX_SDK/lib" \
  --add-modules java.base,java.desktop,jdk.unsupported,javafx.controls,javafx.fxml,javafx.graphics \
  --output "$FINAL_APP_DIR/runtime" \
  --compress 2 \
  --no-header-files \
  --no-man-pages

if [ $? -ne 0 ]; then echo "jlink failed"; exit 1; fi

# 4. Копирование JAR и нативных библиотек JavaFX
echo "--- Copying App and Native Libs ---"
cp "target/$APP_NAME-$APP_VERSION.jar" "$FINAL_APP_DIR/app/app.jar"
cp $JAVAFX_SDK/lib/*.so "$FINAL_APP_DIR/runtime/lib/"

# 5. Создание скрипта запуска
echo "--- Creating launch script ---"
cat > "$FINAL_APP_DIR/start.sh" <<EOL
#!/bin/bash
SCRIPT_DIR="\$(cd "\$(dirname "\${BASH_SOURCE[0]}")" && pwd)"
"\$SCRIPT_DIR/runtime/bin/java" -Dprism.verbose=true -jar "\$SCRIPT_DIR/app/app.jar"
EOL

chmod +x "$FINAL_APP_DIR/start.sh"
chmod +x "$FINAL_APP_DIR/runtime/bin/java"

# 6. Архивация в ZIP
echo "--- Zipping ---"
cd target
# Упаковываем папку DnD_Manager_Linux, внутри которой лежит DnD_Manager
zip -r "${RELEASE_NAME}.zip" "$RELEASE_NAME/"
echo "--- Done! ---"
echo "Archive is at: target/${RELEASE_NAME}.zip"