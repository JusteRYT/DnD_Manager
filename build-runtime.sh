#!/bin/bash

# --- НАСТРОЙКИ ---
APP_NAME="DnD_Manager"
APP_VERSION="1.0-SNAPSHOT"
JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
# Проверь, что этот путь точный!
JAVAFX_SDK="/home/sergey/openjfx-17.0.18_linux-x64_bin-sdk/javafx-sdk-17.0.18"
TARGET_DIR="target/dist_linux"

# 1. Очистка и сборка JAR
echo "--- Building JAR with Maven ---"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then echo "Maven build failed"; exit 1; fi

# 2. Подготовка папок
echo "--- Preparing directories ---"
rm -rf $TARGET_DIR
rm -rf target/runtime  # Очищаем старый рантайм перед сборкой
mkdir -p $TARGET_DIR/app

# 3. Создание Runtime (Java + JavaFX) прямо внутри целевой папки
echo "--- Creating Custom Runtime ---"
$JAVA_HOME/bin/jlink \
  --module-path "$JAVA_HOME/jmods:$JAVAFX_SDK/lib" \
  --add-modules java.base,java.desktop,jdk.unsupported,javafx.controls,javafx.fxml,javafx.graphics \
  --output "$TARGET_DIR/runtime" \
  --compress 2 \
  --no-header-files \
  --no-man-pages

if [ $? -ne 0 ]; then echo "jlink failed"; exit 1; fi

# 4. Копирование JAR и нативных библиотек JavaFX
echo "--- Copying App and Native Libs ---"
cp "target/$APP_NAME-$APP_VERSION.jar" "$TARGET_DIR/app/app.jar"

# КРИТИЧЕСКИЙ ШАГ: Копируем все .so файлы из SDK в папку lib нашего рантайма
cp $JAVAFX_SDK/lib/*.so "$TARGET_DIR/runtime/lib/"

# 5. Создание скрипта запуска и выдача прав
echo "--- Creating launch script ---"
cat > "$TARGET_DIR/start.sh" <<EOL
#!/bin/bash
# Находим реальный путь к папке со скриптом
SCRIPT_DIR="\$(cd "\$(dirname "\${BASH_SOURCE[0]}")" && pwd)"
# Запуск из локального рантайма
"\$SCRIPT_DIR/runtime/bin/java" -Dprism.verbose=true -jar "\$SCRIPT_DIR/app/app.jar"
EOL

# Даем права на запуск скрипту и бинарнику java внутри рантайма
chmod +x "$TARGET_DIR/start.sh"
chmod +x "$TARGET_DIR/runtime/bin/java"

# 6. Архивация в ZIP
echo "--- Zipping ---"
# Переходим в target, чтобы в архиве не было лишних путей
cd target
zip -r "${APP_NAME}_Linux.zip" dist_linux/
echo "--- Done! ---"
echo "Archive is at: target/${APP_NAME}_Linux.zip"