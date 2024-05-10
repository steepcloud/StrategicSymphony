# Strategic Symphony

Strategic Symphony is a multiplayer game server implemented in Java. It allows multiple clients to connect and play a game simultaneously.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Game rules](#game-rules)
- [License](#license)

## Introduction

StrategicSymphony provides a platform for hosting multiplayer games where clients can connect, interact, and play games together in real-time. It's built using Java sockets for communication between the server and clients.

## Features

- Allows multiple clients to connect to the server concurrently.
- Supports real-time gameplay synchronization among connected clients.
- Implements game logic for turn-based interactions.
- Provides features like joker usage and game scoring.

## Installation

To run StrategicSymphony, follow these steps:

1. Clone the repository to your local machine.
2. Make sure you have Java installed.
3. Compile the Java files using a Java compiler.
4. Run the server using the compiled class file.

## Usage

Once the server is running, clients can connect to it using a compatible client application. The server handles game logic and communication between clients. Clients can send commands to the server to perform actions in the game, such as placing pieces or using jokers.

### Building the Project

Before running the project, make sure to modify the `.classpath` file to include the required JARs (byte-buddy, byte-buddy-agent, mockito-core) in the build path. Add the following entries to the `<classpathentry>` section of the `.classpath` file:

```xml
<buildSpec>
 <?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="src" path="src"/>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8"/>
    <!-- Add the following entries for the required JARs -->
    <classpathentry kind="lib" path="libs/byte-buddy.jar"/>
    <classpathentry kind="lib" path="libs/byte-buddy-agent.jar"/>
    <classpathentry kind="lib" path="libs/mockito-core.jar"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
```

## Game rules
- The game is played by at least two and up to five players.
- Each player is assigned a symbol (e.g., color, letter, icon) for identification on the game board.
- Players take turns placing pieces on a 6x10 game board, similar to the game of Go.
- At the beginning of the game, the server randomly places one piece for each player on the board.
- Players make moves in the order they joined the game.
- Each move involves placing a piece on an empty cell adjacent to another cell containing a piece of the same type (color).
- Players start with three jokers, which can be used to bypass the game rules:
- Double Move Joker: Allows the player to place two pieces in a single turn.
- Replace Joker: Allows the player to place a piece on a non-empty cell if adjacent to a cell with the same type of piece.
- Freedom Joker: Allows the player to place a piece on any empty cell.
- A player is considered blocked if they cannot make a move, even with the use of a joker. In this case, they forfeit their turn.
- The game ends when all players are blocked.
- The winner is the player with the most pieces on the board at the end of the game. In case of a tie, the player who made the last move wins.
  
## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
