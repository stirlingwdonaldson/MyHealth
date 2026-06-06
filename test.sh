#!/usr/bin/env sh
set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
BUILD_DIR="$ROOT_DIR/out/test-classes"
MAIN_SOURCES="$ROOT_DIR/out/main-sources.txt"
TEST_SOURCES="$ROOT_DIR/out/test-sources.txt"

if [ -z "${JAVAFX_HOME:-}" ]; then
  echo "Set JAVAFX_HOME to your JavaFX SDK folder, for example:"
  echo "  export JAVAFX_HOME=/path/to/javafx-sdk-21"
  exit 1
fi

if [ -n "${SQLITE_JDBC_JAR:-}" ]; then
  SQLITE_JAR="$SQLITE_JDBC_JAR"
else
  SQLITE_JAR="$ROOT_DIR/lib/sqlite-jdbc.jar"
fi

if [ -n "${JUNIT_CONSOLE_JAR:-}" ]; then
  JUNIT_JAR="$JUNIT_CONSOLE_JAR"
else
  JUNIT_JAR="$ROOT_DIR/lib/junit-platform-console-standalone.jar"
fi

if [ ! -f "$SQLITE_JAR" ]; then
  echo "SQLite JDBC jar not found. Set SQLITE_JDBC_JAR or place sqlite-jdbc.jar in lib/."
  exit 1
fi

if [ ! -f "$JUNIT_JAR" ]; then
  echo "JUnit console jar not found. Set JUNIT_CONSOLE_JAR or place junit-platform-console-standalone.jar in lib/."
  exit 1
fi

mkdir -p "$BUILD_DIR"
find "$ROOT_DIR/src/main/java" -name "*.java" > "$MAIN_SOURCES"
find "$ROOT_DIR/src/test/java" -name "*.java" > "$TEST_SOURCES"

javac \
  --module-path "$JAVAFX_HOME/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$SQLITE_JAR:$JUNIT_JAR" \
  -d "$BUILD_DIR" \
  @"$MAIN_SOURCES" @"$TEST_SOURCES"

cp "$ROOT_DIR/src/main/resources"/*.fxml "$BUILD_DIR"/

java \
  --module-path "$JAVAFX_HOME/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -jar "$JUNIT_JAR" \
  execute \
  --class-path "$BUILD_DIR:$SQLITE_JAR" \
  --scan-class-path
