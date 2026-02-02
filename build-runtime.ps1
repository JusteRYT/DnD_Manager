$JAVA_HOME="C:\Program Files\Java\jdk-21"
$JAVAFX_SDK="C:\javafx-sdk-20"

jlink `
--module-path "$JAVA_HOME\jmods;$JAVAFX_SDK\lib" `
--add-modules java.base,javafx.controls,javafx.fxml `
--output target\runtime\windows `
--compress 2 `
--no-header-files `
--no-man-pages
