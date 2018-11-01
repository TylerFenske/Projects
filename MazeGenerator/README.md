## About:
A small C program that uses recursion to generate a random maze, then output it to a manually generated bitmap. This program was written by
Tyler Fenske, and all art was created by a friend for the purpose of this project, David Crum. 

## How to use the program:
To create a maze bitmap, run mazetest, altering the mazeGenerate() function as desired.

If specified, a waypoint will be generated in the maze that ENSURES the solution to the maze will pass though that coordinate of the maze.

mazeSolve() will alter the maze to show the solution to the maze.

mazePrint() will create the bitmap of the maze, printing with no solution if mazeSolve() was not called beforehand, and printing with 
the solution if mazeSolve() was called beforehand.

mazeGenerate() parameters:
int width (the width of the maze), int height (the height of the maze), int wayPointX (the x coordinate of the wayPoint), int wayPointY 
(the y coordinate of the wayPoint), the following params are unused for this version, and should be set to 0 when passing as arguments.

