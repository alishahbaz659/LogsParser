# LogsParser

JavaFX application that reconstructs ordered message chains from randomized pipeline logs using linked-list traversal algorithms.

## Features

- Multi-format log parsing with regex validation
- Real-time hex-to-ASCII decoding
- Reverse-order message reconstruction
- GUI with file loading capabilities
- Cross-platform native packaging

## Quick Start

```bash
./mvnw javafx:run
```

## Input Format
```
pipeline_id id encoding [body] next_id
```

**Example:**
```
2 3 1 [4F4B] -1
1 0 0 [some text] 1
1 1 0 [another text] 2
```

## Tech Stack

- Java 17 + JavaFX 19
- Maven build system
- Lombok, JUnit 5

## Requirements

Java 17+ | Maven 3.6+ 