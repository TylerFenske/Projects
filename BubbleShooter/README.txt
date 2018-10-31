Game Play:
- Start the game by hitting the button at the bottom of the screen.
- Pause the game at any time by pressing the button at the bottom of the screen.
- Resume the game by pressing the button at the bottom of the screen.
- Move the magic pointer left or right using the left and right arrow keys.
- Launch Crystal Balls by hitting the space bar when the pointer is pointed at a desired location.

Game Win/Lose Conditions:
- To win the game, clear every ball from the board by either shattering, or making them fall.
- The first way to lose is to have a ball lock in place at or below the fire line.
- The second way to lose is to reach a debt total of $750 or more.
** The objective of the game is to clear all balls without ever placing a ball in the fire, but in few enough turns that you don't exceed the debt limit. The lower your score, the better you played! **

Game Scoring:
- For every ball shattered (when 3 or more balls match in color they will shatter), the player will gain 6 "points" also known as Debt.
- For every ball that falls (when a ball no longer is connected to the top row because another row was shattered that was keeping it connected, it falls) the player will only gain 2 "points" also known as Debt.

Description of Classes:
*BubbleShooter - This class contains the main methods used for launching the game at a specified frame size.
*BubbleFrame - This class constructs the GUI JFrame and ties all GUI components together. It determines when the game is won, or lost, and updates the players points on the screen. Also contains text and audioclips to help tell the story of the game.
*RightPanel - Class that defines the right panel of the game's GUI.
*LeftPanel - Class that defines the left panel of the game's GUI.
*Bubble - Bubble object that contains only a String and a getter method for the String. String is used to represent bubble on game board.
*BubbleManager - BubbleManager class that has useful methods for BubbleShooter game. Contains methods that allow filling the board, adding bubbles, removing matching bubbles, and removing any floating bubbles remaining. Added functionality that allows placing bubbles in cardinal directions in relation to other already existing bubbles.
*GamePanel - Class that contains all information for the GamePanel GUI to operate. Constructs lists of points to map out the GUI coordinates of bubbles, and works with the BubbleManager class to constantly repaint the board to match the state of the game.

Alorithm Details:
- Generating random bubbles - Random bubbles are generated with the Random class in BubbleManager, then are passed to GamePanel to display and be played with on the GUI.
- Shooting bubble and managing it's position - Bubble is launched by starting a timer, and position is managed with local movingX and movingY variables until it collides with a stickable bubble or the top of the board.
- Detecting and removing matching bubbles - When a moving bubble collides with a stickable bubble or the top of the board, the program with determine the exact point of collision, then reference a list that provides the "master point" aka the point where the image of the stickable bubble that was intersected was drawn. BubbleManager then runs methods of removing matching bubbles by looking at all neighboring bubbles, and any that match, add them to another list. Then repeat the process for each matching bubble until no more match. The list is then looped over and removes all matching bubbles from the board.
- Detecting and removing floating bubbles - Once bubbles have been removed, the floating bubble method is called, which will search the entire board for any bubbles that are not connected to the top. A method in BubbleManager will look over the entire board and make a list of all bubbles connected to the top. If a bubble isn't connected to the top in any way, it is removed from the board.
- Detecting end of game - The gameIsOver boolean is set to true when any bubble is added to the bottom row (Size of row + blank spaces - 1) in any col.

Extras:
Added Images, sound, and a unique scoring system that is described above.

Easter Eggs: The background image has several gamer/anime references such as:
- Spell Book contains:
	- A Pokeball
	- A Four Star DragonBall
	- A Real Easter Egg
	- Frostmourne rune on book (under next random ball on left page)
- Baby Onyxia Drake next to Tish
- Frostmourne runes on scoreboard

- The pause button doubles as a way to interact with the characters of the game and learn some of the lore of the universe!

Known Bugs:
- Physics definitely aren't perfect, but they work well enough to make the game playable. It's likely that the GUI (moving ball) is too computation heavy, and can cause sluggish performance on lower performing machines. 
- Sound will sometimes glitch out and repeat itself or just not play at all when it should. Especially when the space bar is spammed nonsensically. 

Future Features: If game development continues, the following features will likely be added.
- Multiple, increasingly difficult levels.
- Levels with controlled ball setup on the board and a controlled pattern of "next ball" to launch to provide for a challenging puzzle-like game.
- Better sounds
- A way to restart the game without exiting.
- Special balls that can do special things such as a bomb ball that destroys all balls of that color.
