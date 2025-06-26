# Undercooked
*Overcooked but Underdeveloped*

<img src="https://github.com/user-attachments/assets/37073690-0e74-454f-ac2c-99d17930c56d" align="right" width="380" style="margin-left: 20px; margin-bottom: 0;"/>

Undercooked is a Java Swing single-player restaurant simulation game. You play as a doner chef fulfilling customer orders by picking up, preparing, and delivering the right orders before time runs out.
<br />
<br />

> How to Run
>
Run the game via `Launcher.java` which is located at: `source/main/Launcher.java`

<br clear="all"/>



## Structure

```
undercooked/
├── assets/                # Images, sounds, and map files 
│   ├── entity/            # Player sprites 
│   ├── objects/           # Item icons (doner, tomato, etc.) 
│   ├── sounds/            # Game sound effects (also volume button PNGs) 
│   └── tiles/             # Map tiles and tile-based items 
├── source/ 
│   ├── entity/            # Player logic<br>
│   ├── main/              # GamePanel, StartScreen, core logic 
│   └── tile/              # TileManager and map rendering
```


## Game Features

- Single-slot inventory system  
- Customer request system  
- Countdown timer per request 
- If the timer runs out, the customer changes their order
- Sound effects for key events (level passed, customer lost, etc.)  
- Cuttable ingredients: Tomato and lettuce must be cut before being used  
- Station interaction system: Use combination stations (marked with a square) to assemble doner wraps from ingredients  
- 3 levels:  
  - **Level 1**: Introductory level - serve 2 customers, 1 minute per customer.
  - **Level 2**: Faster player speed - serve 3 costumers.
  - **Level 3**: Shorter timer - serve 5 customers, 30 seconds per customer.

## How to Play

- Move your character using **WASD**  
- Press **E** to pick up or place an item  
- Use **cutting stations** to cut tomato and lettuce  
- Use **combination stations** (marked with a square center) to assemble doner wraps  
- Deliver the completed order by placing it near the **delivery tile**

<br />
A mid-game screenshot:  
<br />

<p align="center">
  <img src="https://github.com/user-attachments/assets/0269a828-9dcb-47a8-b6b1-e9e7f839f2b5" width="500" />
</p>

### Order Abbreviations

These abbreviations appear in the customer orders and are also shown in the in-game "Rules" section:

- `donerw` = doner + wrap  
- `donerl` = doner + wrap + lettuce  
- `donert` = doner + wrap + tomato  
- `donerlt` = doner + wrap + lettuce + tomato  

## Acknowledgments
Up until the initial commit, this project was collaboratively developed with @egeuyguner [https://github.com/egeuyguner]

All the player, object and tile assets were created by me. <br>
Note: Look close enough and you might see a plate appear on the chef's hand when the inventory is full!
