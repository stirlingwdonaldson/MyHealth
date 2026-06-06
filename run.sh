#!/usr/bin/env sh
set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
BUILD_DIR="$ROOT_DIR/out/classes"
SOURCE_LIST="$ROOT_DIR/out/sources.txt"

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

if [ ! -f "$SQLITE_JAR" ]; then
  echo "SQLite JDBC jar not found. Either:"
  echo "  1. Place sqlite-jdbc.jar at $ROOT_DIR/lib/sqlite-jdbc.jar"
  echo "  2. Or set SQLITE_JDBC_JAR=/path/to/sqlite-jdbc.jar"
  exit 1
fi

mkdir -p "$BUILD_DIR"
find "$ROOT_DIR/src/main/java" -name "*.java" > "$SOURCE_LIST"

javac \
  --module-path "$JAVAFX_HOME/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$SQLITE_JAR" \
  -d "$BUILD_DIR" \
  @"$SOURCE_LIST"

cp "$ROOT_DIR/src/main/resources"/*.fxml "$BUILD_DIR"/

java \
  --module-path "$JAVAFX_HOME/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$BUILD_DIR:$SQLITE_JAR" \
  donaldson.stirling.a2.Main
