## Game Instruction:
The game is a 2 player tank battle. You and your opponent will each control a tank on the battlefield.
The objective of the game is to destroy the other player's tank.
### Keybindings:
#### Tank Movement:
*   ↑ Move Forward: W (for player 1) or NUMPAD 8 (for player 2)
*   | Move Backward: S (for player 1) or NUMPAD 5 (for player 2)
*   ← Rotate Left: A (for player 1) or NUMPAD 4 (for player 2)
*   → Rotate Right: D (for player 1) or NUMPAD 6 (for player 2)
#### Tank Turret:
*   \< Rotate Left: G (for player 1) or LEFT ARROW (for player 2)
*   \> Rotate Right: J (for player 1) or RIGHT ARROW (for player 2)
*   \* Shoot: H (for player 1) or UP ARROW (for player 2)

## Configuration
* JLayer .jar file must be added to the project as a library.
  It is shipped with source files.
* The app must be able to find **TanksFiles** directory inside **%userprofile%/My drive/**,
to run properly.  
  The **TanksFiles** folder is shipped with the app.

## About the game engine:
* Allows construction of arbitrary (though hardware-bound) amount of
  arbitrary (though parameter-bound) tanks and projectiles.
* `Tank` class parameters include
    + Hull with texture, radius, engine
    + Turret with texture, engine, projectile traits, own keybindings
    + Keybindings
    + Health
    + ...
* `Projectile` class parameters include
    + Skin
    + Velocity
    + Damage
    + Initial coordinates
