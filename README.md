# LogsParser

JavaFX (GUI) desktop application that reconstructs ordered message chains from randomized pipeline logs using reverse chain traversal reconstruction algorithm.

## Features

- Multi-format log parsing with regex validation
- hex-to-ASCII decoding
- Reverse-order message reconstruction
- GUI with file loading capabilities

## IntelliJ Quick Start (JDK 11+)

To run directly in **IntelliJ IDEA**:

1. Open the project and set the **Project SDK** to **JDK 11 or higher**.
2. Ensure Maven imports all dependencies.
3. Right-click the main class (e.g., `LogsParser`) and select **Run**.

JavaFX is managed via Maven, so no manual setup is needed.

## Input Format
```
pipeline_id id encoding [body] next_id
```

**Example input:**
```
2 3 1 [4F4B] -1
1 0 0 [some text] 1
1 1 0 [another text] 2
```
**Example Output:**
```
Pipeline 2
    3| OK
    99| OK
Pipeline 1
    2| body
    1| another text
    0| some text
```

## Tech Stack

- Java 11 + JavaFX 19 (GUI)
- Maven build system
- Lombok (code automation), JUnit 5 (testing)

## Requirements

Java 11+ | Maven 3.6+ 
