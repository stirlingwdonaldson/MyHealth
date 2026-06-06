@echo off
setlocal

set "ROOT_DIR=%~dp0"
set "BUILD_DIR=%ROOT_DIR%out\test-classes"
set "MAIN_SOURCES=%ROOT_DIR%out\main-sources.txt"
set "TEST_SOURCES=%ROOT_DIR%out\test-sources.txt"

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

if "%JUNIT_CONSOLE_JAR%"=="" (
  set "JUNIT_JAR=%ROOT_DIR%lib\junit-platform-console-standalone.jar"
) else (
  set "JUNIT_JAR=%JUNIT_CONSOLE_JAR%"
)

if not exist "%SQLITE_JAR%" (
  echo SQLite JDBC jar not found. Set SQLITE_JDBC_JAR or place sqlite-jdbc.jar in lib\.
  exit /b 1
)

if not exist "%JUNIT_JAR%" (
  echo JUnit console jar not found. Set JUNIT_CONSOLE_JAR or place junit-platform-console-standalone.jar in lib\.
  exit /b 1
)

if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
dir /s /b "%ROOT_DIR%src\main\java\*.java" > "%MAIN_SOURCES%"
dir /s /b "%ROOT_DIR%src\test\java\*.java" > "%TEST_SOURCES%"

javac --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -cp "%SQLITE_JAR%;%JUNIT_JAR%" -d "%BUILD_DIR%" @"%MAIN_SOURCES%" @"%TEST_SOURCES%"
if errorlevel 1 exit /b 1

copy /Y "%ROOT_DIR%src\main\resources\*.fxml" "%BUILD_DIR%" > nul

java --module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml -jar "%JUNIT_JAR%" execute --class-path "%BUILD_DIR%;%SQLITE_JAR%" --scan-class-path
