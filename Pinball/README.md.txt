Project: Pinball
Author: Tyler Fenske
Class; CS351
Date: 08/29/2018

The GameCoordinator class is the main class that contains the main program loop. 
There are no command line arguments to this program.

This program is a game called Pinball. The game has two modes, Reset, and Play,
which can be toggled via buttons on the screen. 

Once the program has been executed, the game will immediately begin in Reset
mode. While in reset mode, the user can move the red ball on the screen left or
right as much as they like. When the user has found a suitable position for the
ball, the user may switch the game to Play mode by pressing the Yellow Play 
button.

When the play button is pressed, the play button will turn grey and inactive, 
and the reset button will turn yellow and active. The ball will launch in a 
random direction, bouncing off any walls it encounters. 

In the background, there are blue and orange tiles. If the ball flies over
an orange tile, the tile will turn blue, then reward the user with 10 points. 
The users total points is displayed at the bottom of the screen, in red letters
between the reset and play buttons. 

Once the ball has touched 3 walls without hovering over an orange tile, the
game will pause. If you user would like to keep playing, they may enter reset
mode again by pressing the reset button. A new board will be generated, the
ball will be set back to its starting position, but the score will carry over.

BUGS: The ball may glide over the corner of an orange tile unnoticed, as the 
detection for the ball isn't quite sophisticated enough to detect every pixel
of the ball, but actually just the center. This causes some tiles to appear
as if they should have turned blue when they actually do not.