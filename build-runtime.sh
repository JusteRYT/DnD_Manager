#!/bin/bash

# Полная JDK 21
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

# Путь к JavaFX SDK, который нужно скачать отдельно
JAVAFX_SDK=/home/sergey/openjfx-17.0.18_linux-x64_bin-sdk/javafx-sdk-17.0.18/

# Проверка jlink
if [ ! -f "$JAVA_HOME/bin/jlink" ]; then
    echo "jlink not found! Make sure JAVA_HOME points to full JDK 21."
    exit 1
fi

# Проверка JavaFX SDK
if [ ! -d "$JAVAFX_SDK/lib" ]; then
    echo "JavaFX SDK not found in $JAVAFX_SDK"
    exit 1
fi

# Создаём минимальный runtime
$JAVA_HOME/bin/jlink \
  --module-path $JAVA_HOME/jmods:$JAVAFX_SDK/lib \
  --add-modules java.base,javafx.controls,javafx.fxml \
  --output target/runtime/linux \
  --compress 2 \
  --no-header-files \
  --no-man-pages

echo "Runtime with JavaFX created at target/runtime/linux"
