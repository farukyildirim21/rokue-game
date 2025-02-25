
# Project Title: COMP302 Project - Rokue-Like Game

![image](https://github.com/user-attachments/assets/037b4d54-0a28-4a1b-87e9-9dc2f299889d)


## Description
This project is a collaborative development of a Rokue-like game designed as part of the COMP302 coursework. It is an interactive game that integrates advanced game mechanics, creative graphics, and modular software design principles.

## Features
- **Dynamic Gameplay:** Players navigate through levels with challenges, monsters, and rewards.
- **Enchantment System:** Equip special items with unique effects.
- **Build Mode:** Create and customize game levels.
- **Inventory Management:** Track and utilize collected items.
- **Sound Effects:** Immersive audio for a realistic gaming experience.
- **Save and Load:** Players can save their progress and resume later.
- **Wizard Monster Behavior:** Includes adaptive strategies based on the hero's situation.

## Gameplay Overview
- The hero explores dungeon halls (Earth, Air, Water, Fire) to collect runes that unlock doors to the next hall.
- Enchantments like extra lives, luring gems, and cloaks of protection help the hero avoid or escape monsters.
- Monsters include archers, fighters, and wizards, each with unique behaviors.
- The game ends when the hero loses all lives or successfully finds all runes and exits the dungeon.

## Project Structure
- **`src/enchantment`**: Contains classes for enchantment functionalities, such as Cloak of Protection, Extra Life, and more.
- **`src/monster`**: Implements different monster types and behaviors, including the Strategy pattern for adaptive wizard behavior.
- **`src/ui`**: Includes the main game interface and supporting utilities.
- **`src/panel`**: Manages various panels like the inventory and hall interface.
- **`src/player`**: Defines player attributes and management.
- **`src/test`**: Unit tests for various game functionalities.
- **`src/assets`**: Contains visual and audio assets, including sprites, tilesets, and sounds.

## How to Play
1. Launch the game: Build Mode or Play Mode.
2. In Build Mode, design the dungeon halls by placing objects based on minimum criteria:
   - Hall of Earth: At least 6 objects.
   - Hall of Air: At least 9 objects.
   - Hall of Water: At least 13 objects.
   - Hall of Fire: At least 17 objects.
3. In Play Mode:
   - Navigate through the dungeon using arrow keys.
   - Click on objects to find runes and collect enchantments.
   - Avoid or distract monsters while completing each hall.

## Teammates
- **Faruk Yıldırım**
- **Yıldız Temizkan**
- **Uğur Genç**
- **Tolga Aksoydan**
- **Doruk Yalçın**

