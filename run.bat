@echo off
setlocal

set "ROOT_DIR=%~dp0"
set "BUILD_DIR=%ROOT_DIR%out\classes"
set "SOURCE_LIST=%ROOT_DIR%out\sources.txt"

if "%JAVAFX_HOME%"=="" (
  echo Set JAVAFX_HOME to your JavaFX SDK folder, for example:
  echo   set JAVAFX_HOME=C:\path\to\javafx-sdk-21
  exit /b 1
)

if "%SQLITE_JDBC_JAR%"=="" (
  set "SQLITE_JAR=%ROOT_DIR%lib\sqlite-jdbc.jar"
) else (
  set "SQLITE_JAR=%SQLITE_JDBC_JAR%"
)

if not exist "%SQLITE_JAR%" (
  echo SQLite JDBC jar not found. Either:
  echo   1. Place sqlite-jdbc.jar at %ROOT_DIR%lib\sqlite-jdbc.jar
  echo   2. Or set SQLITE_JDBC_JAR=C:\path\to\sqlite-jdbc.jar
  exit /b 1
)

if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
dir /s /b "%ROOT_DIR%src\main\java\*.java" > "%SOURCE_LIST%"

javac --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -cp "%SQLITE_JAR%" -d "%BUILD_DIR%" @"%SOURCE_LIST%"
if errorlevel 1 exit /b 1

copy /Y "%ROOT_DIR%src\main\resources\*.fxml" "%BUILD_DIR%" > nul

java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -cp "%BUILD_DIR%;%SQLITE_JAR%" donaldson.stirling.a2.Main
