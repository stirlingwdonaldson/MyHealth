# MyHealth

Student Number: s4169686
COSC2391 Further Programming Assignment 2.

## Run Instructions

Run on macOS/Linux from the repo root:

```bash
export JAVAFX_HOME=/path/to/javafx-sdk-21
./run.sh
```

Run on Windows from the repo root:

```bat
set JAVAFX_HOME=C:\path\to\javafx-sdk-21
run.bat
```

The application creates `MyHealth.db` automatically if the file does not already exist.

Run automated tests from the root with the JUnit console jar. Either place `junit-platform-console-standalone.jar` in `lib/`, or set `JUNIT_CONSOLE_JAR` to the jar path.

```bash
./test.sh
```

## Assignment Checklist

// Y Signup
// Y Login
// Y Dashboard displays the user first name and last name
// Y User profile can be edited
// Y Health records can be created
// Y Health record validation is implemented
// Y Health records can be edited
// Y Health records can be deleted
// Y Data is stored and restored using JDBC with SQLite
// Y User can view all health records
// Y User can export records to a text file
// Y User password update implemented
// Y Password hashing/encryption implemented: SHA-256 hashing
// Y JUnit test cases included
// Y Design pattern implemented in addition to MVC: Repository/DAO
// Y Database setup/run instructions: Run `./run.sh` or `run.bat`; `MyHealth.db` is created automatically.
