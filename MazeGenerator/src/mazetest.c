/*
  Maze testing file
*/

/* Includes */
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "mazegen.h"


const int DIRECTION_LIST[] = {NORTH, EAST, SOUTH, WEST};
const int DIRECTION_DX[]   = {    0,    1,     0,   -1};
const int DIRECTION_DY[]   = {   -1,    0,     1,    0};


const unsigned char pipeList[] =
{
  219, 208, 198, 200, 210, 186, 201, 204,
  181, 188, 205, 202, 187, 185, 203, 206
};



//===========================================================================
//Prints escape characters to change terminal foreground color.
void textcolor(int color)
{
  //30	Black
  //31	Red
  //32	Green
  //33	Yellow
  //34	Blue
  //35	Magenta
  //36	Cyan
  //37	White
  printf("%c[%d;%d;%dm", 0x1B, 0, color, 40);
}




int main(void)
{
  //srand sets the seed of the random number generator in <stdio.h>.
  //NOTE: srand should only be called ONCE per PROGRAM.
  //NOTE: Seeding with the current time makes a different sequence of
  //      random numbers each time the program is run - assuming it is not
  //      started more than once during the same millisecond.
  srand((unsigned)time(NULL));

  //For testing, you may want to comment out the srand(time) call above
  //   and set the seed to a constant so that you can repeat the same
  //   random sequece.
  //srand(16);
  //
//  printf("TEST 1: generate and print basic random mazes.\n");
//  printf("    Efficiency and memory usage are not checked.\n");
//  printf("    The two (50,30) mazes must be different.\n");
//
//
//  //int mazeGenerate(int width, int height,
//  //   int wayPointX, int wayPointY, int wayPointAlleyLength,
//  //   double wayPointDirectionPercent, double straightProbability,
//  //   int printAlgorithmSteps);
////
//  mazeGenerate(3,3,   2,2,0,  1.0,  0.0, FALSE);
//  mazePrint();
//  mazeSolve(); mazePrint();
//
//
//  mazeGenerate(20,11,   10,5,0,  1.0,  0.0, TRUE);
//  mazePrint();
//
//
//
//  mazeGenerate(20,11,   18,5,4,  1.0,  0.0, TRUE);
//  mazePrint();
//
//
//  mazeGenerate(20,11,   10,5,4,  0.2,  0.0, TRUE);
//  mazePrint();
//  mazeSolve(); mazePrint();
//
//
//  mazeGenerate(20,11,   10,5,2,  1.0,  0.0, TRUE);
//  mazePrint();
//  mazeSolve(); mazePrint();
//
//
//  return 0;
//
//  mazeGenerate(40,22,   20,11,0,  1.0,  0.0, TRUE);
//  mazePrint();
//  mazeSolve(); mazePrint();
//
//  mazeGenerate(40,22,   20,11,0,  1.0,  0.0, FALSE);
//  mazeSolve(); mazePrint();
//
//
//
//
//  printf("TEST 2: Four mazes. \n");
//  printf(" To pass, memory must not leak.\n");
//
//  mazeGenerate(75,40,   20,11,0,  1.0,  0.0, FALSE);
//  mazePrint();
//
//  mazeGenerate(20,20,   20,11,0,  1.0,  0.0, FALSE);
//  mazePrint();
//
//  mazeGenerate(40,22,   20,11,0,  1.0,  0.0, FALSE);
//  mazePrint();
//
//  mazeGenerate(75,40,   35,20,0,  1.0,  0.0, FALSE);
//  mazePrint();
//
//  mazeFree();
//
//
//  printf("TEST 3: Super stress test:\n");
//  printf("  Must take no longer than\n");
//  printf("  30 seconds on cs.moons.unm.edu\n");
//  printf("  An inefficient algorithm will show.\n");
//
//  int i;
//  for (i=0; i < 50*14; i++)
//  { if( i%50 == 0)
//    { printf("Made %d mazes so far.\n",i);
//    }
//    mazeGenerate(450,450,   225,225,0,  1.0,  0.0, FALSE);
//  }
//

mazeGenerate(45, 20, 40, 10, 0, 0, 0, FALSE);
mazeSolve();
mazePrint();

//
//  printf("TEST 4: Error cases:\n");
//  mazeGenerate(90,2,   40,1,0,  1.0,  0.0, FALSE);
//  mazeGenerate(30,30,  31,10,0,  1.0,  0.0, FALSE);
//  mazeGenerate(30,30,  15,0,0,  1.0,  0.0, FALSE);
//
//
  // Release memory
  mazeFree();
//
//
//  //There is no need to call mazeFree a second time, but doing so should
//  //  not crash the program.
  mazeFree();



  /* Program finished properly */
  return FALSE;
}
