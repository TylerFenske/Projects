/**********************************************************/
/* Tyler Fenske                                           */
/*                                                        */
/* Generates a random maze with given size and waypoint.  */
/* If mazeSolve() is called before mazePrint(), the       */
/* maze will be printed with green pipes representing     */
/* the solution. The waypoint is always printed with a red*/
/* pipe.                                                  */
/**********************************************************/


#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "mazegen.h"

#define BUFFER 16
#define SOLUTION 32
#define WAYPOINT 64
#define NIL 128

#define HEADER_SIZE 54
#define PIXEL_WIDTH 8
#define PIXEL_HEIGHT 8
#define BYTES_PER_PIXEL 3


unsigned int ** maze;
unsigned char * mazeBitmap;

int northEntranceUncarved = 1;
int southEntranceUncarved = 1;
int wayPointControl = 1;

//not including buffer
int numRows = 0;
int numCols = 0;

//including buffer
int rowCount = 0;
int colCount = 0;

int showSolution = 0;
int carvingUpper = 0;
int carvingLower = 0;
int allocatedMemory = 0;

int byteData = PIXEL_HEIGHT * PIXEL_WIDTH * BYTES_PER_PIXEL;


/**************************************************************/
/* Array of structures.                                       */
/* Each array is filled with RGB values for each "pipe" image */
/* used to construct the maze as a bitmap                     */
/**************************************************************/
struct PipeImages
{
  unsigned char RGBValues[PIXEL_HEIGHT*PIXEL_WIDTH*BYTES_PER_PIXEL];
}pipes[16];


/********************************************************/
/* Params: unsigned char * a                            */
/* Input: unsigned char * / Output: int                 */
/* Converts reversed significance of a number in bytes  */
/* to it's appropriate value, and returns as an int.    */
/********************************************************/
int byteArrayToInt(unsigned char* a)
{
  return a[0] | (a[1]<<8) | (a[2]<<16) | (a[3]<<24);
}


/********************************************************/
/* Params: char * imageName, unsigned char * pipeRGB    */
/* Input: unsigned char *, char * / Output: void        */
/* Reads in the image name passed, into the global      */
/* array contained in a struct                          */
/********************************************************/
void readInImage(char * imageName, unsigned char * pipeRGB)
{

  FILE * fp = fopen(imageName, "rb");

  unsigned char header[HEADER_SIZE];

  fread(header, sizeof(char), HEADER_SIZE, fp);

  int imageWidth = byteArrayToInt(&header[18]);
  int imageHeight = byteArrayToInt(&header[22]);

  int rowBytes = imageWidth*BYTES_PER_PIXEL;

  int dataSize = imageHeight*rowBytes;

  fread(pipeRGB, sizeof(char), dataSize, fp);


  fclose(fp);

}


/**********************************************************/
/* Params: void                                           */
/* Input: void / Output: void                             */
/* Fills each struct array with RGB data for each "pipe"  */
/* image, in the same order as pipelist[] from mazetest.c */
/**********************************************************/
void BuildPipeStructs(void)
{
  readInImage("NoDIR.bmp", pipes[0].RGBValues);
  readInImage("N.bmp", pipes[1].RGBValues);
  readInImage("E.bmp", pipes[2].RGBValues);
  readInImage("NE.bmp", pipes[3].RGBValues);
  readInImage("S.bmp", pipes[4].RGBValues);
  readInImage("NS.bmp", pipes[5].RGBValues);
  readInImage("ES.bmp", pipes[6].RGBValues);
  readInImage("NES.bmp", pipes[7].RGBValues);
  readInImage("W.bmp", pipes[8].RGBValues);
  readInImage("NW.bmp", pipes[9].RGBValues);
  readInImage("EW.bmp", pipes[10].RGBValues);
  readInImage("NEW.bmp", pipes[11].RGBValues);
  readInImage("SW.bmp", pipes[12].RGBValues);
  readInImage("NSW.bmp", pipes[13].RGBValues);
  readInImage("ESW.bmp", pipes[14].RGBValues);
  readInImage("NESW.bmp", pipes[15].RGBValues);
}


/********************************************************/
/* Params: unsigned int num                             */
/* Input: unsigned int / Output: unsigned char          */
/* Returns the last byte of a 4 byte number.            */
/* This is used to build mazeBitmap with reversed       */
/* byte significance.                                   */
/********************************************************/
unsigned char getFirstByte(unsigned int num)
{

  unsigned int temp = num << 24;
  temp = temp >> 24;

  unsigned char result = (char) temp;
  return result;
}


/********************************************************/
/* Params: unsigned int num                             */
/* Input: unsigned int / Output: unsigned char          */
/* Returns the second to last byte of a 4 byte number.  */
/* This is used to build mazeBitmap with reversed       */
/* byte significance.                                   */
/********************************************************/
unsigned char getSecondByte(unsigned int num)
{

  unsigned int temp = num << 16;
  temp = temp >> 24;

  unsigned char result = (char) temp;
  return result;
}


/********************************************************/
/* Params: unsigned int num                             */
/* Input: unsigned int / Output: unsigned char          */
/* Returns the third to last byte of a 4 byte number.   */
/* This is used to build mazeBitmap with reversed       */
/* byte significance.                                   */
/********************************************************/
unsigned char getThirdByte(unsigned int num)
{

  unsigned int temp = num << 8;
  temp = temp >> 24;

  unsigned char result = (char) temp;
  return result;
}


/********************************************************/
/* Params: unsigned int num                             */
/* Input: unsigned int / Output: unsigned char          */
/* Returns the first byte of a 4 byte number.           */
/* This is used to build mazeBitmap with reversed       */
/* byte significance.                                   */
/********************************************************/
unsigned char getFourthByte(unsigned int num)
{

  unsigned int temp = num;
  temp = temp >> 24;

  unsigned char result = (char) temp;
  return result;
}


/**********************************************************/
/* Params: void                                           */
/* Input: void / Output: void                             */
/* Fills mazeBitmap array with the first 54 bytes of data */
/**********************************************************/
void fillHeader(void)
{
  FILE * fp;

  unsigned char header1[2] = {'B', 'M'};
  unsigned char header2[12] = {0, 0, 0, 0, 54, 0, 0, 0, 40, 0, 0, 0};
  unsigned char header3[8] = {1, 0, 24, 0, 0, 0, 0, 0};
  unsigned char header4[16] = {19, 11, 0, 0, 19, 11, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0};

  unsigned int totalData =
    (PIXEL_HEIGHT * PIXEL_WIDTH * BYTES_PER_PIXEL * numCols * numRows)
      + HEADER_SIZE;

  unsigned int totalImageWidth = PIXEL_WIDTH * numCols;

  unsigned int totalImageHeight = PIXEL_HEIGHT * numRows;

  unsigned int totalRGBData =
    PIXEL_HEIGHT * PIXEL_WIDTH * BYTES_PER_PIXEL * numCols * numRows;

  unsigned char totalDataBytesReversed[4];
  totalDataBytesReversed[0] = getFirstByte(totalData);
  totalDataBytesReversed[1] = getSecondByte(totalData);
  totalDataBytesReversed[2] = getThirdByte(totalData);
  totalDataBytesReversed[3] = getFourthByte(totalData);

  unsigned char totalImageWidthBytesReversed[4];
  totalImageWidthBytesReversed[0] = getFirstByte(totalImageWidth);
  totalImageWidthBytesReversed[1] = getSecondByte(totalImageWidth);
  totalImageWidthBytesReversed[2] = getThirdByte(totalImageWidth);
  totalImageWidthBytesReversed[3] = getFourthByte(totalImageWidth);

  unsigned char totalImageHeightBytesReversed[4];
  totalImageHeightBytesReversed[0] = getFirstByte(totalImageHeight);
  totalImageHeightBytesReversed[1] = getSecondByte(totalImageHeight);
  totalImageHeightBytesReversed[2] = getThirdByte(totalImageHeight);
  totalImageHeightBytesReversed[3] = getFourthByte(totalImageHeight);

  unsigned char totalRGBDataBytesReversed[4];
  totalRGBDataBytesReversed[0] = getFirstByte(totalRGBData);
  totalRGBDataBytesReversed[1] = getSecondByte(totalRGBData);
  totalRGBDataBytesReversed[2] = getThirdByte(totalRGBData);
  totalRGBDataBytesReversed[3] = getFourthByte(totalRGBData);

  fp = fopen("mazeBitmap.bmp", "wb");

  fwrite(header1, sizeof(char), 2, fp);

  fwrite(totalDataBytesReversed, sizeof(char), 4, fp);

  fwrite(header2, sizeof(char), 12, fp);

  fwrite(totalImageWidthBytesReversed, sizeof(char), 4, fp);

  fwrite(totalImageHeightBytesReversed, sizeof(char), 4, fp);

  fwrite(header3, sizeof(char), 8, fp);

  fwrite(totalRGBDataBytesReversed, sizeof(char), 4, fp);

  fwrite(header4, sizeof(char), 16, fp);

  fclose(fp);
}


/****************************************************/
/* Params: void                                     */
/* Input: void / Output: void                       */
/* Uses free() to free memory allocated by malloc().*/
/* Returns nothing                                  */
/****************************************************/
void mazeFree(void)
{
  if(allocatedMemory)
  {
    int i;
    for (i=0; i<rowCount; i++)
    {
      free(maze[i]);
    }
    free(maze);
    allocatedMemory = 0;
  }
}


/****************************************************/
/* Params: int row, int col                         */
/* Input: int int / Output: int                     */
/* Uses malloc() to dynamically allocate memory to  */
/* a 2D array that holds the cells of the maze.     */
/* Returns 0 if malloc returns null.                */
/* Returns 1 if malloc successfully allocates mem.  */
/****************************************************/
int allocateMazeMemory(int rows, int cols)
{

  maze = malloc(cols * sizeof(unsigned int*));

  if(maze == NULL)
  {
    return 0;
  }

  int c;
  for(c=0; c<cols; c++)
  {
    maze[c] = malloc(rows * sizeof(unsigned int));

    if(maze == NULL)
    {
      return 0;
    }
  }

  allocatedMemory = 1;
  return 1;
}




/****************************************************/
/* Params: int cols, int rows                       */
/* Input: int int / Output: void                    */
/* Set's all elements in 2D array to 0, except for  */
/* the outer edges. The edges are assigned a buffer */
/* value.                                           */
/* Returns nothing.                                 */
/****************************************************/
void buildUncarvedMaze(int cols, int rows)
{
  int c, r;
  for(c=0; c<cols; c++)
  {
    for(r=0; r<rows; r++)
    {
      if(r==0 || r==rows-1 || c==0 || c==cols-1)
      {
        maze[r][c] = BUFFER;
      }else
      {
        maze[r][c] = 0;
      }
    }
  }
}


/*********************************************************/
/* Params: void                                          */
/* Input: void / Output: void                            */
/* Switches showSolution to TRUE so that when mazePrint()*/
/* is called, it will display the solution.              */
/* Returns nothing.                                      */
/*********************************************************/
void mazeSolve(void)
{
  showSolution = 1;
}


/**********************************************************/
/* Params: void                                           */
/* Input: void / Output: void                             */
/* Appends all RGB data that was stored in mazeBitmap     */
/* to the mazeBitmap.bmp file.                            */
/**********************************************************/
void finalizeBitmap(void)
{
  FILE * fp;

  fp = fopen("mazeBitmap.bmp", "ab");

  fwrite(mazeBitmap, sizeof(char),
    PIXEL_HEIGHT*PIXEL_WIDTH*BYTES_PER_PIXEL*numCols*numRows, fp);

  fclose(fp);
}

/**************************************************************/
/* Params: void                                               */
/* Input: void / Output: void                                 */
/* Prints maze as bitmap image with all white "pipes", except */
/* the waypoint, which is printed as a red "pipe".            */
/* Returns nothing.                                           */
/**************************************************************/
void mazePrintWithoutSolution(void)
{
  unsigned char * p_mazeBitmap = mazeBitmap;

  int r, c, x, y;

  for(r=rowCount-1; r>=0; r--)
  {
    for(y=0; y<PIXEL_WIDTH; y++)
    {
      for(c=0; c<colCount; c++)
      {
        for(x=0; x < PIXEL_WIDTH * BYTES_PER_PIXEL; x++)
        {
          if(maze[r][c] != BUFFER)
          {
            if(maze[r][c] > SOLUTION && maze[r][c] < WAYPOINT)
            {
              *p_mazeBitmap = pipes[maze[r][c]-SOLUTION].
                RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)];
              p_mazeBitmap++;
            }else if(maze[r][c] > WAYPOINT)
            {
              if(pipes[maze[r][c]-WAYPOINT-SOLUTION].
                RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)] == 255)
              {
                *p_mazeBitmap = 0;
                *(p_mazeBitmap+1) = 0;
                *(p_mazeBitmap+2) = 255;

                p_mazeBitmap+=3;
                x+=2;
              }else
              {
                *p_mazeBitmap = pipes[maze[r][c]-WAYPOINT-SOLUTION].
                  RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)];
                p_mazeBitmap++;
              }
            }else
            {
              *p_mazeBitmap = pipes[maze[r][c]].
                RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)];
              p_mazeBitmap++;
            }
          }
        }
      }
    }
  }
  finalizeBitmap();
}



/**********************************************************/
/* Params: void                                           */
/* Input: void / Output: void                             */
/* Prints maze as bitmap with all white "pipes", except   */
/* the waypoint, which is printed as a red "pipe",        */
/* and the solution, which is printed as green "pipes"    */
/* Returns nothing.                                       */
/**********************************************************/
void mazePrintWithSolution(void)
{
  unsigned char * p_mazeBitmap = mazeBitmap;

  int r, c, x, y;

  for(r=rowCount-1; r>=0; r--)
  {
    for(y=0; y<PIXEL_WIDTH; y++)
    {
      for(c=0; c<colCount; c++)
      {
        for(x=0; x < PIXEL_WIDTH * BYTES_PER_PIXEL; x++)
        {
          if(maze[r][c] != BUFFER)
          {
            if(maze[r][c] > SOLUTION && maze[r][c] < WAYPOINT)
            {
              if(pipes[maze[r][c]-SOLUTION].
                RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)] == 255)
              {
                *p_mazeBitmap = 0;
                *(p_mazeBitmap+1) = 255;
                *(p_mazeBitmap+2) = 0;

                p_mazeBitmap+=3;
                x+=2;
              }else
              {
                *p_mazeBitmap = pipes[maze[r][c]-SOLUTION].
                  RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)];
                p_mazeBitmap++;
              }
            }else if(maze[r][c] > WAYPOINT)
            {
              if(pipes[maze[r][c]-WAYPOINT-SOLUTION].
                RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)] == 255)
              {
                *p_mazeBitmap = 0;
                *(p_mazeBitmap+1) = 0;
                *(p_mazeBitmap+2) = 255;

                p_mazeBitmap+=3;
                x+=2;
              }else
              {
                *p_mazeBitmap = pipes[maze[r][c]-WAYPOINT-SOLUTION].
                  RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)];
                p_mazeBitmap++;
              }
            }else
            {
              *p_mazeBitmap = pipes[maze[r][c]].
                RGBValues[x + (PIXEL_WIDTH * BYTES_PER_PIXEL * y)];
              p_mazeBitmap++;
            }
          }
        }
      }
    }
  }

  finalizeBitmap();
}


/*************************************************************************/
/* Params: void                                                          */
/* Input: void / Output: void                                            */
/* If showSolution is set to TRUE, function calls mazePrintWithSolution()*/
/* If showSolution is set to FALSE, function calls                       */
/* mazePrintWithoutSolution().                                           */
/* Returns nothing.                                                      */
/*************************************************************************/
void mazePrint(void)
{

  BuildPipeStructs();
  fillHeader();

  if(!showSolution)
  {
    mazePrintWithoutSolution();
  }else if(showSolution)
  {
    mazePrintWithSolution();
  }
}


/***************************************************************/
/* Params: int dirMoved, int row, int col                      */
/* Input: int int int / Output: void                           */
/* Checks what direction was randomly selected in mazeCarve(), */
/* then updates the array by using bitwise | to turn on the bit*/
/* of the appropriate cardinal directions.                     */
/* Returns nothing.                                            */
/***************************************************************/
void updateMaze(int dirMoved, int row, int col)
{
  wayPointControl = 1;

  if(dirMoved == NORTH)
  {
    maze[row+1][col] |= NORTH;
    maze[row][col] |= SOUTH;
  }else if(dirMoved == EAST)
  {
    maze[row][col-1] |= EAST;
    maze[row][col] |= WEST;
  }else if(dirMoved == WEST)
  {
    maze[row][col+1] |= WEST;
    maze[row][col] |= EAST;
  }else if(dirMoved == SOUTH)
  {
    maze[row-1][col] |= SOUTH;
    maze[row][col] |= NORTH;
  }
}


/******************************************************************/
/* Params: int row, int col, int lastRow                          */
/* Input: int int int / Output: int                               */
/* Checks to see if currently carving the upper or lower half     */
/* of the maze. Then, if the maze has reached the upper or lower  */
/* boundary, and has yet to carve an exit/entrance, it will do so.*/
/* Returns 1 if exit/entrance was carved.                         */
/* Returns 0 if function didn't make changes to array.            */
/******************************************************************/
int carvedEntrance(int row, int col, int lastRow)
{
  if(carvingUpper)
  {
    if(northEntranceUncarved && row==1)
    {
      northEntranceUncarved = 0;
      maze[row][col] |= NORTH;
      maze[row][col] |= SOLUTION;
      return 1;
    }
  }else if(carvingLower)
  {
    if(southEntranceUncarved && row==lastRow)
    {
      southEntranceUncarved = 0;
      maze[row][col] |= SOUTH;
      maze[row][col] |= SOLUTION;
      return 1;
    }
  }
  return 0;
}


/****************************************************************************/
/* Params: int row, int col, int dirMoved, int wayPointRow, int lastRow     */
/* Input: int int int int int / Output: int                                 */
/* Using recursion, this method will randomly call itself with new          */
/* coordinates (moving 1 space N,E,S, or W), until the entire array         */
/* has been filled with connecting pipe characters. The process begins      */
/* at the specified waypoint, then builds both halves of the maze from there*/
/* to assure the waypoint is part of the solution.                          */
/* Returns 1 if current location in array is part of the solution.          */
/* Returns 0 if current location in array is not part of the solution.      */
/****************************************************************************/
int carveMaze(int row, int col, int dirMoved, int wayPointRow, int lastRow)
{
  short coords[] = {NORTH, EAST, SOUTH, WEST};
  static short direction = 0;
  short partOfSolution = 0;


  if(carvingLower)
  {
    if((maze[row][col] != 0 && wayPointControl) || row<wayPointRow)
    {
      return partOfSolution;
    }
  }else if(carvingUpper)
  {
    if((maze[row][col] != 0 && wayPointControl) || row>wayPointRow)
    {
      return partOfSolution;
    }
  }


  if(!wayPointControl)
  {
    maze[row][col] |= WAYPOINT;
  }

  updateMaze(dirMoved, row, col);


  if(carvedEntrance(row, col, lastRow))
  {
    partOfSolution = 1;
  }


  while(coords[0] != NIL || coords[1] != NIL ||
    coords[2] != NIL || coords[3] != NIL)
  {
    int r = rand();
    int roll = r%(4);

    if(coords[roll] != NIL)
    {
      direction = coords[roll];
      coords[roll] = NIL;

      if(direction == NORTH)
      {
        if(carveMaze(row-1, col, NORTH, wayPointRow, lastRow))
        {
          partOfSolution = 1;
        }
      }else if(direction == EAST)
      {
        if(carveMaze(row, col+1, EAST, wayPointRow, lastRow))
        {
          partOfSolution = 1;
        }
      }else if(direction == WEST)
      {
        if(carveMaze(row, col-1, WEST, wayPointRow, lastRow))
        {
          partOfSolution = 1;
        }
      }else if(direction == SOUTH)
      {
        if(carveMaze(row+1, col, SOUTH, wayPointRow, lastRow))
        {
          partOfSolution = 1;
        }
      }
    }
  }


  if(partOfSolution)
  {
    maze[row][col] |= SOLUTION;
  }


  return partOfSolution;
}


/**************************************************************************/
/* Params: int width, int height, int wayPointX, int wayPointY            */
/* Other params unused for this milestone of maze project.                */
/* Input: int int int int / Output: int                                   */
/* First frees all allocated memory, then checks if waypoint is closer to */
/* the top or bottom. If closer to the top, carving will begin towards the*/
/* bottom, and vice versa.                                                */
/* If given waypoint isn't in the bounds of the given size of maze,       */
/* or if array size causes malloc() to return null due to an exceedingly  */
/* high request for heap storage, an error message will print to std out. */
/* Width:Columns Height:Rows // +2 on width and height for buffer.        */
/* Returns 1 if error was encountered.                                    */
/* Returns 0 if no errors were encountered.                               */
/**************************************************************************/
int mazeGenerate(int width, int height, int wayPointX, int wayPointY,
                 int wayPointAlleyLength, double wayPointDirectionPercent,
                 double straightProbability, int printAlgorithmSteps)

{

  mazeFree();
  free(mazeBitmap);

  showSolution = 0;
  northEntranceUncarved = 1;
  southEntranceUncarved = 1;

  numRows = height;
  numCols = width;
  int cols = width+2;
  int rows = height+2;
  colCount = cols;
  rowCount = rows;
  int errorsFound = 0;



  if(width<3 || height <3)
  {
    errorsFound = 1;
    printf("\nError: Maze must be initialized with a"
           "minimum width and height of 3.\n");
    return errorsFound;
  }

  if(wayPointX > width || wayPointY > height
    || wayPointX <= 0 || wayPointY <= 0)
  {
    errorsFound = 1;
    printf("\nError: Specified waypoint exceeds "
           "the bounds of specified maze size.\n");
    return errorsFound;
  }

  if(!(allocateMazeMemory(cols, rows)))
  {
    errorsFound = 1;
    printf("\nError: Unable to allocate the memory requested.\n");
    return errorsFound;
  }

  buildUncarvedMaze(cols, rows);


  if(wayPointY >= (rows+1)/2 )
  {
    carvingUpper = 1;
    carveMaze(wayPointY, wayPointX, NIL, wayPointY, height);
    carvingUpper = 0;
    carvingLower = 1;
    wayPointControl = 0;
    carveMaze(wayPointY, wayPointX, NIL, wayPointY, height);
    carvingLower = 0;
  }else
  {
    carvingLower = 1;
    carveMaze(wayPointY, wayPointX, NIL, wayPointY, height);
    carvingLower = 0;
    carvingUpper = 1;
    wayPointControl = 0;
    carveMaze(wayPointY, wayPointX, NIL, wayPointY, height);
    carvingUpper = 0;
  }

  mazeBitmap=malloc(PIXEL_HEIGHT*PIXEL_WIDTH*BYTES_PER_PIXEL*numRows*numCols);

  return errorsFound;
}
