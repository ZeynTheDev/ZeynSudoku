
# Zeyn Sudoku

Read in: [Bahasa Indonesia](README.id.md)

Zeyn Sudoku is a native desktop Sudoku puzzle game built with Java and JavaFX. This application is designed with a clean interface, smooth functionality, and features on par with professional game standards.

## Key Features

- **Difficulty Levels:** Three game modes available (Easy, Medium, Hard) that determine the number of initial clues and the hint quota.
- **Smart Hint System:** Automated assistance to fill empty boxes with the correct answer, with quotas adjusted based on difficulty (Easy: 10, Medium: 6, Hard: 3).
- **Real-time Error Detection:** The system automatically highlights conflicting numbers in rows, columns, or 3x3 areas in red.
- **Time Management:** Features a timer to track completion time. Includes a Pause feature that covers the board with a blurred screen (anti-cheat) when activated.
- **Save & Continue:** The game automatically saves when the player returns to the Main Menu. Players can resume (Continue) exactly where and when they left off.
- **Auto-Switch Input:** The number pad automatically switches to the next available number if the quota for the currently selected number is exhausted on the board.

## Technologies Used

- **Language:** Java 17
- **GUI Framework:** JavaFX 25
- **Build Tool:** Apache Maven
- **Architecture:** Functional separation of UI (FXML), Controllers, Game Logic, and File Management (I/O).

## System Requirements

To run or compile this project locally, ensure your system has:
- Java Development Kit (JDK) 17 or newer.
- Apache Maven installed and configured in your environment variables.

## How to Run Locally

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/ZeynTheDev/ZeynSudoku.git
   ```
2. Navigate to the project directory:
   ```bash
   cd ZeynSudoku
   ```
3. Run the application using Maven:
   ```bash
   mvn clean javafx:run
   ```

## How to Build Executables

This project is configured to be packaged into a standalone executable (`.jar`, `.exe`, or `.dmg`) using Maven plugins and JPackage.

To build a standalone `.jar` (Fat JAR):
```bash
mvn clean package
```
The `.jar` file will be available inside the `target/` directory.

> [!NOTE]
> This repository also utilizes GitHub Actions to automatically release Windows `.exe` and macOS `.dmg` installers on the Releases page whenever a new version tag is pushed


## License

This project is licensed under MIT License.