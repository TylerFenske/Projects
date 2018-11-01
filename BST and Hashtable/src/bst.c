/**
* Author: Tyler Fenske
* Partner: Tony Nguyen
*
* This program takes a .txt file delimited by new lines, with integer data,
* and stores it in a newly built (written from scratch) Binary Search Tree.
*
* The program then allows the user to insert, search, delete, or display
* from the console.
*
* Each of the main functions {building, inserting, searching, and deletion}
* provide a runtime analysis to the console.
**/


#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <string.h>


/**
* Each element of our bst is a Struct node
* that consists of a value, and pointer to both of it's children.
**/
struct node
{
    int value;
    struct node* leftChild;
    struct node* rightChild;
};


//text file w/dataSet, read in from cmd line arg
char* dataSet;

//used to keep track of runtime
time_t start, end;

//Only turns on if command line was input incorrectly
short helpFlag = 0;


/**
* Reads in data set's file name from command line args.
**/
void readCommandLine(int argc, char **argv)
{
    int c;

    while ( (c = getopt(argc, argv, "f:")) != -1)
    {
        switch(c)
        {
            case 'f':
                dataSet = optarg;
                break;
            case '?':
                helpFlag = 1;
                break;
            default:
                helpFlag = 1;
        }
    }
}


/**
* Creates and allocates memory for a new node.
* Used in insertChain().
**/
struct node* createNewNode(int value)
{
    struct node* newNode = (struct node*)malloc(sizeof(struct node));

    newNode->value = value;
    newNode->leftChild = NULL;
    newNode->rightChild = NULL;

    return newNode;
};


/**
* This function takes the right child of the node we are trying to delete,
* and returns descendant with the smallest value.
**/
struct node* findMinDescendant (struct node* root)
{
    if(root->leftChild == NULL)
    {
        return root;
    }

    root = findMinDescendant(root->leftChild);

    return root;
};


/**
* Deletes a value/node from the binary search tree using recursion.
*
* There are 3 cases for deletion, all handled differently.
*
* CASE 1: Node being deleted has no children
* Simply delete and free memory for x.
*
* CASE 2: Node being deleted has only one child
* The address of the node's child is returned to the node's parent
* as it's new child.
*
* CASE 3: Node being deleted has two children
* First finds the address of the minimum node, in the node's right
* subtree. Then sets the value of the node being deleted, to the min
* node's value. Finally, recursively calls delete on the min node.
**/
struct node* deleteChain (struct node* root, int value)
{

    if(root == NULL)
    {
        return root;
    }
    else if(value < root->value)
    {
        root->leftChild = deleteChain(root->leftChild, value);
    }
    else if(value > root->value)
    {
        root->rightChild = deleteChain(root->rightChild, value);
    }
    else
    {
        //CASE 1: Node has no children
        if(root->rightChild == NULL && root->leftChild == NULL)
        {
            free(root);
            return NULL;
        }
        //CASE 2: Node has only one child
        else if(root->leftChild == NULL)
        {
            struct node* onlyChild = root->rightChild;
            free(root);
            return onlyChild;
        }
        else if(root->rightChild == NULL)
        {
            struct node* onlyChild = root->leftChild;
            free(root);
            return onlyChild;
        }
        //CASE 3: Node has two children
        else
        {
            struct node* min = findMinDescendant(root->rightChild);
            root->value = min->value;
            root->rightChild = deleteChain(root->rightChild, min->value);
        }
    }

    return root;
}


/**
* Searches to see if value is already in the bst using recursion.
* Returns 1 if it is.
* Else returns 0.
**/
short searchChain (struct node* root, int value)
{
    short result;

    if(root == NULL)
    {
        return 0;
    }
    if(value == root->value)
    {
        return 1;
    }
    else if(value < root->value)
    {
        result = searchChain(root->leftChild, value);
    }
    else if(value > root->value)
    {
        result = searchChain(root->rightChild, value);
    }

    return result;
}


/**
* Inserts the input value into the binary search tree
* using recursion.
**/
struct node* insertChain(struct node* root, int value)
{

    if(root == NULL)
    {
        root = createNewNode(value);
    }
    else if(value < root->value)
    {
        root->leftChild = insertChain(root->leftChild, value);
    }
    else if(value > root->value)
    {
        root->rightChild = insertChain(root->rightChild, value);
    }

    return root;
}


/**
* Prints the bst to the console.
* Node Value => The integer value stored at that node, also used to identify the node.
* Height => The height of the current node in the bst.
*
* Example:
*               6           height = 0
*             5   7         height = 1
*           3       9       height = 2
*         2   4   8         height = 3
**/
void displayUtil(struct node* root, int height)
{
    if(root == NULL)
    {
        return;
    }

    printf("\n%d\t%d\t\n", height, root->value);

    height++;

    displayUtil(root->rightChild, height);
    displayUtil(root->leftChild, height);
}


/**
* Starting function for display that initializes the
* height to 0, and prints the header of the display.
**/
void displayChain(struct node* root)
{
    printf("\nHeight\tNode Value\n\n");
    displayUtil(root, 0);
}


/**
* Reads in one line of dataset at a time, and inserts the
* value read in from the file if it's not already in the tree.
**/
struct node* readOneLine(char* singleLine, struct node* root)
{
    int val = atoi(singleLine);

    return insertChain(root, val);

}


/**
* If value user enters exists in tree, it is deleted.
* Prints the runtime for deletion.
**/
void userDelete (struct node* root)
{
    int de, result;
    //scanf detects newline, so need x var to trash the newline
    char keepGoing, x;
    double time;

    do
    {
        printf("\n\n\nWhat number would you like to delete out of the bst?\n");
        scanf("%d", &de);

        result = searchChain(root, de);

        if(result)
        {
            start = clock();
            deleteChain(root, de);
            end = clock();
            printf("Value %d deleted from the tree.\n", de);
        }
        else
        {
            printf("Value not found, no entries deleted.\n");
        }

        time = (double)(end-start)/CLOCKS_PER_SEC;

        if(time == 0)
        {
            printf("\nRuntime for deletion: shorter than .000001 seconds.\n\n");
        }
        else
        {
            printf("\nRuntime for deletion in seconds: %f\n\n", time);
        }

        printf("Keep Deleting?\n"
               "y for yes\n");

        scanf("%c%c", &x, &keepGoing);

        }while(keepGoing == 'y');
}


/**
* The user is notified whether or not the value the user entered exists in the tree.
* Prints the runtime for searching.
**/
void userSearch (struct node* root)
{
    int se, result;
    char keepGoing, x;
    double time;

    do
    {
        printf("\n\n\nWhat number would you like to search the bst for?\n");
        scanf("%d", &se);

        start = clock();
        result = searchChain(root, se);
        end = clock();

        if(result)
        {
            printf("Search found value %d stored in the tree.\n", se);
        }
        else
        {
            printf("Search found no results.\n");
        }

        time = (double)(end-start)/CLOCKS_PER_SEC;

        if(time == 0)
        {
            printf("\nRuntime for search: shorter than .000001 seconds.\n\n");
        }
        else
        {
            printf("\nRuntime for search in seconds: %f\n\n", time);
        }

        printf("Keep Searching?\n"
               "y for yes\n");

        scanf("%c%c", &x, &keepGoing);

        }while(keepGoing == 'y');
}


/**
* If value user enters doesn't exist in the tree, it is inserted.
* Prints the runtime for insertion.
**/
void userInsert (struct node* root)
{
    int ins;
    char keepGoing, x;
    double time;

    do
    {
        printf("\n\n\nWhat number would you like to insert into the bst?\n");

        scanf("%d", &ins);

        start = clock();
        if(!(searchChain(root, ins)))
        {
            insertChain(root, ins);
            end = clock();
            printf("\nValue %d inserted into tree.\n", ins);
        }
        else
        {
            end = clock();
            printf("\nValue %d already exists in the tree.\n", ins);
        }

        time = (double)(end-start)/CLOCKS_PER_SEC;

        if(time == 0)
        {
            printf("\nRuntime for insert: shorter than .000001 seconds.\n\n");
        }
        else
        {
            printf("\nRuntime for insert in seconds: %f\n\n", time);
        }

        printf("Keep Inserting?\n"
               "y for yes\n");

        scanf("%c%c", &x, &keepGoing);

        }while(keepGoing == 'y');
}


/**
* Gives the user options to pick from to make changes to the bst.
**/
void userControl(struct node* root)
{
    char userInput;

    while(userInput != 'q')
    {
        printf("\n\n\nWhat would you like to do now?\n\n\n"
               "Enter one of the following commands:\n\n"
               "Key Press\tAction\n\n"
               "s\t\tSearch Tree\n"
               "i\t\tInsert in Tree\n"
               "d\t\tDelete from Tree\n"
               "p\t\tPrint Tree Info\n"
               "q\t\tQuit Program\n\n");


        scanf("\n%c", &userInput);

        switch(userInput)
        {
            case 's':
                userSearch(root);
                break;
            case 'i':
                userInsert(root);
                break;
            case 'd':
                userDelete(root);
                break;
            case 'p':
                displayChain(root);
                break;
            case 'q':
                break;
            default:
                printf("\n\n\nPlease choose an appropriate option {s, i, d, p, q}\n");
                break;

        }
    }
}


/**
* Frees all memory allocated for the bst nodes.
**/
void freeChain(struct node* root)
{
    if(root->leftChild != NULL)
    {
        freeChain(root->leftChild);
    }
    if(root->rightChild != NULL)
    {
        freeChain(root->rightChild);
    }

    free(root);
    return;
}


/**
* Main function to control building and manipulating the bst.
**/
int main(int argc, char **argv)
{
    readCommandLine(argc, argv);

    if(helpFlag || argc == 1)
    {
        printf("Please run program in the following format:\n\n"
               "./bst -f DataSetn.txt\n\n");
        return 0;
    }

    double runtime;

    if(dataSet != NULL)
    {
        start = clock();

        FILE* fPointer = fopen(dataSet, "r");

        if(!fPointer)
        {
            printf("\nPlease rerun program with a valid file name.\n\n");
            return 0;
        }

        char singleLine[100];

        fgets(singleLine, 100, fPointer); //first line of file is the header which denotes the size of dataset which isn't useful for the bst, so we trash it
		fgets(singleLine, 100, fPointer); //second line of file is the first value of the data set, we use this to initialize our root
		
		struct node* root = NULL;
		root = readOneLine(singleLine, root); //returns the starting address to an allocated block of memory in the heap, and sets our root pointer to that address.


        while (fgets(singleLine, 100, fPointer)) //reads from fPointer till \n is found, stores everything in singleLine array
        {
            readOneLine(singleLine, root); //updates the bst by inserting each item from the file line by line
        }
        fclose(fPointer);

        end = clock();

        printf("\nBinary Search Tree built successfully!");

        runtime = (double)((end-start)/CLOCKS_PER_SEC);

        if(runtime == 0)
        {
            printf("\nRuntime to build tree: shorter than .000001 seconds.\n");
        }
        else
        {
            printf("\nRuntime to build tree in seconds: %f\n", runtime);
        }

        userControl(root);

        freeChain(root);
    }



    return 0;
}


