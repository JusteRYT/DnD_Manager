#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Software rendering + classpath
"$DIR/target/runtime/linux/bin/java" \
  -Dprism.order=sw \
  -Djavafx.platform=Monocle \
  -Dmonocle.platform=Headless \
  -cp "$DIR/target/DnD_Manager-1.0-SNAPSHOT.jar" \
  com.example.dnd_manager.MainApp