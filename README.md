 🎮 Tic-Tac-Toe — Java Swing Edition

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-007396?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)

**A feature-rich, dark-themed Tic-Tac-Toe desktop game built entirely with Java Swing.**  
Supports multiple board sizes, AI opponents with 3 difficulty levels, persistent score tracking, and game history logging.

</div>

---

## ✨ Features

| Feature | Details |
|---|---|
| 🎨 **Dark UI Theme** | Sleek dark color palette with custom-painted buttons |
| 🧩 **3 Board Sizes** | 3×3 (Classic), 5×5 (Medium), 7×7 (Large) |
| 🤖 **AI Opponent** | Easy, Medium, and Hard difficulty levels |
| 👥 **2-Player Mode** | Local multiplayer support |
| 🔐 **Login System** | Password login + Secret Question authentication |
| 💾 **Score Persistence** | Player scores saved to file across sessions |
| 📜 **Game History** | All match results logged with timestamps |
| ♟️ **Minimax AI** | Full minimax with alpha-beta pruning for Hard mode |

---

## 📸 Preview

```
┌──────────────────────────────┐
│   TIC-TAC-TOE  ✦  Main Menu │
│                              │
│  ▶️   Play Game              │
│  📜  View History            │
│  ✕   Exit                   │
└──────────────────────────────┘
```

> **UI is rendered natively using Java Swing with custom `paintComponent` graphics.**

---

## 🚀 Getting Started

### Prerequisites

- Java **JDK 8** or higher installed
- Any terminal / command prompt

### 🔧 Compile

```bash
javac TicTacToe.java
```

### ▶️ Run

```bash
java TicTacToe
```

---

## 🔐 Default Login Credentials

The game includes a simple authentication screen on startup.

| Method | Credential |
|---|---|
| **Username** | `admin` |
| **Password** | `1234` |
| **Secret Question** (Pet's name) | `fluffy` |

> You can modify these in the `showLoginDialog()` method inside `TicTacToe.java`.

---

## 🤖 AI Difficulty Levels

### 😊 Easy
Plays completely random valid moves.

### 🤔 Medium
- Wins immediately if possible (70% of the time)
- Blocks opponent from winning
- Prefers center cell

### 😈 Hard
- **3×3 board:** Uses full **Minimax algorithm with Alpha-Beta pruning** — unbeatable
- **5×5 / 7×7 boards:** Uses **heuristic-based depth-limited search** with threat counting
- Always wins immediately or blocks threats

---

## 🗂️ Project Structure

```
TicTacToe.java
│
├── TicTacToe          # Main JFrame — game flow, UI, menus
├── GameButton         # Custom JButton with painted X/O symbols
├── Board              # Game logic, win detection, move validation
├── Player (abstract)  # Base class for players
│   ├── HumanPlayer    # Human input handler
│   └── ComputerPlayer # AI logic (Easy / Medium / Hard)
└── FileHandler        # Score persistence & history logging
```

---

## 💾 Saved Data

| File | Contents |
|---|---|
| `scores.txt` | Player names and their cumulative win counts |
| `game_history.log` | Timestamped match results (winner + participants) |

Both files are created automatically in the same directory as the `.java` file on first run.

**Example `game_history.log` entry:**
```
[2025-06-08 14:32:11] Alice vs Computer  →  Winner: Alice
[2025-06-08 14:35:07] Alice vs Bob       →  Winner: Draw
```

---

## 🎨 Color Theme Reference

| Role | Color |
|---|---|
| Background | `#0F0F19` |
| Card Background | `#191928` |
| X Symbol | `#63DDB7` (Teal) |
| O Symbol | `#FF6B6B` (Red) |
| Accent / Highlights | `#7B61FF` (Purple) |
| Text | `#E6E6F5` |

---

## 🛠️ Tech Stack

- **Language:** Java (JDK 8+)
- **GUI Framework:** Java Swing
- **AI Algorithm:** Minimax with Alpha-Beta Pruning
- **Persistence:** File I/O (`BufferedReader` / `PrintWriter`)
- **Concurrency:** `SwingWorker` for non-blocking AI moves

---

## 📌 How to Play

1. Launch the game with `java TicTacToe`
2. Log in using the credentials above
3. Choose your **board size**
4. Enter **Player 1's name**
5. Select **vs Computer** or **vs Player**
6. If vs Computer, pick a **difficulty level**
7. Click cells to make your move
8. First to get **3 (or 4) in a row** wins!
9. Scores persist — come back and keep your streak! 🔥

---

## 👤 Author

**Syed Nahiyan**

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">
  <i>Made with ☕ Java and a lot of Swing magic.</i>
</div>
g README.md…]()
