/**
* Author: Tyler Fenske
* Partner: Tony Nguyen
*
* This program takes a .txt file delimited by new lines, with integer data,
* and stores it in a newly built (written from scratch) Hash Table.
*
* The program then allows the user to insert, search, delete, or display
* from the console.
*
* Each of the main functions {building, inserting, searching, and deletion}
* provide a runtime analysis to the console.
*
* The hash function used for generating the keys is randomized.
**/


#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <string.h>


/**
* Each element of our hash table is a Struct node
* that consists of a key, value, and pointer to a linked list.
**/
struct node
{
    int key;
    int value;
    struct node* next;
};

//array of nodes which represents the hash_Table
struct node* hash_Table;

//hash function variables
int a, b, p, size;

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
* Helper function to calculate the hash function.
* Returns the modulus (rather than the c operand % which calculates the remainder and can be negative.)
**/
int mod (int x, int m)
{
    return (x % m + m) % m;
}


/**
* Since we need a prime number > 1.5*n (where n is the data set size),
* rather than having a very large list of primes, only provide a list
* of primes > our known dataset sizes.
**/
int getPrimeNumber(void)
{
    //data set sizes            25,   1500,   25000,  100000,   250000,   1000000
    //1.5*n = hashTable size    37,   2250,   37500,  150000,   375000,   1500000
    //first prime number > hashTable size
    int primeNumbers[6] = {41, 2251, 37501, 150001, 375017, 1500007};

    int i;
    for(i = 0; i < 6; i++)
    {
        if(primeNumbers[i] >= size)
        {
            return primeNumbers[i];
        }
    }

    return primeNumbers[5];
}


/**
* Outputs a random number to assist making a randomized
* hash function.
**/
int getRandomNumber(void)
{
    int r, roll;

    r = rand();
    roll = (r%500)+1;

    return roll;
}


/**
* Takes the value as input x, and outputs:
* (ax + b mod p) mod m
* where a and b are random constants, m is the size of the hashTable,
* and p is the first prime number > size of the hash table.
**/
int hashFunction(int x)
{
    return mod(mod(a*x+b, p), size);
}


/**
* Deletes a value/node (val) from the hash table.
*
* If the node being delete is the first node in the linked list, but not the last,
* copies data from the next element of the linked list into the current node, then deletes
* the next node.
*
* If the node being deleted is the first and only node in the linked list, simply sets
* all values back to initial values. {value=0, next=NULL)
*
* If the node being deleted is somewhere in the middle of the linked list, deletes the
* node, and connects the previous to the next node.
*
* If the node being deleted is at the end of the linked list, but not the first node,
* simply deletes the entire node.
*
**/
int deleteChain (int val)
{
    int key = hashFunction(val);

    struct node* current = &hash_Table[key];
    struct node* previous = NULL;

    while(1)
    {
        if(current->value == val)
        {
            if(previous == NULL)
            {
                if(current->next != NULL)
                {
                    hash_Table[key].value = current->next->value;
                    hash_Table[key].next = current->next->next;

                    free(current->next);
                    return key;
                }

                current->value = 0;
                current->next = NULL;
                return key;
            }

            if(current->next != NULL)
            {
                previous->next = current->next;
            }
            else
            {
                previous->next = NULL;
            }

            free(current);
            return key;
        }

        if(current->next == NULL)
        {
            return 0;
        }

        previous = current;
        current = current->next;
    }

    return 0;
}


/**
* Searches to see if val is already in the hash table.
* Returns the key of the location if found.
* Else returns 0.
**/
int searchChain (int val)
{
    int key = hashFunction(val);

    struct node* current = &hash_Table[key];

    while(1)
    {
        if(current->value == val)
        {
            return current->key;
        }

        if(current->next == NULL)
        {
            return 0;
        }

        current = current->next;
    }

    return 0;
}


/**
* Inserts the input val into the hash table at the calculated key.
* Handles collision by inserting into a linked list that has a starting node
* hash_table[key].
**/
void insertChain(int val)
{
    int key = hashFunction(val);

    if(hash_Table[key].value == 0)
    {
        hash_Table[key].value = val;
    }
    else
    {
        struct node* current = &hash_Table[key];

        while(current->next != NULL)
        {
            current = current->next;
        }

        struct node* newNode = (struct node*)malloc(sizeof(struct node));

        newNode->next = NULL;
        newNode->value = val;
        newNode->key = key;

        current->next = newNode;
    }

}

/**
* Prints the hash table to the console.
* Chain => The number of links out the value is stored from the first node of that key.
* Key => The key generated by hashFunction(value).
* Value => The value stored in this node.
**/
void displayChain(void)
{
    printf("\n\nChain\tKey\tValue\n");

    int i;
    for(i = 0; i < size; i++)
    {
        struct node* current = &hash_Table[i];
        int chain = 0;

        while(current->next != NULL)
        {
            printf("%d\t%d\t%d\n", chain, current->key, current->value);
            current = current->next;
            chain++;
        }

        printf("%d\t%d\t%d\n", chain, current->key, current->value);
    }


}


/**
* Initializes the hash table with starting values.
**/
void initializeHashTable(void)
{
    hash_Table = (struct node*)malloc(size*sizeof(struct node));

    int i;
    for(i=0; i<size; i++)
    {
        hash_Table[i].value = 0;
        hash_Table[i].next = NULL;
        hash_Table[i].key = i;
    }
}


/**
* Reads in one line of dataset at a time, and searches the hashtable
* to see if value already exists. If it doesn't it is inserted.
**/
void readOneLine(char* singleLine)
{
    int val = atoi(singleLine);

    if(!searchChain(val))
    {
        insertChain(val);
    }
}


/**
* If value user enters exists in table, it is deleted.
* Prints the runtime for deletion.
**/
void userDelete (void)
{
    int de, result;
    //scanf detects newline, so need x var to trash the newline
    char keepGoing, x;
    double time;

    do
    {
        printf("\n\n\nWhat number would you like to delete out of the hash table?\n");
        scanf("%d", &de);

        start = clock();
        result = deleteChain(de);
        end = clock();

        if(result)
        {
            printf("Value %d at key %d deleted.\n", de, result);
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
* The user is notified whether or not the value the user entered exists in the table.
* Prints the runtime for searching.
**/
void userSearch (void)
{
    int se, result;
    char keepGoing, x;
    double time;

    do
    {
        printf("\n\n\nWhat number would you like to search the hash table for?\n");
        scanf("%d", &se);

        start = clock();
        result = searchChain(se);
        end = clock();

        if(result)
        {
            printf("Search found value %d at key %d.\n", se, result);
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
* If value user enters doesn't exist in the table, it is inserted.
* Prints the runtime for insertion.
**/
void userInsert (void)
{
    int ins;
    char keepGoing, x;
    double time;

    do
    {
        printf("\n\n\nWhat number would you like to insert into the hash table?\n");

        scanf("%d", &ins);

        start = clock();
        if(!(searchChain(ins)))
        {
            insertChain(ins);
            end = clock();
            printf("\nValue %d inserted at key %d\n", ins, hashFunction(ins));
        }
        else
        {
            end = clock();
            printf("\nValue %d already exists in the table at key %d.\n", ins, hashFunction(ins));
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
* Gives the user options to pick from to make changes to the hash table.
**/
void userControl(void)
{
    char userInput;

    while(userInput != 'q')
    {
        printf("\n\n\nWhat would you like to do now?\n\n\n"
               "Enter one of the following commands:\n\n"
               "Key Press\tAction\n\n"
               "s\t\tSearch Table\n"
               "i\t\tInsert in Table\n"
               "d\t\tDelete from Table\n"
               "p\t\tPrint Table\n"
               "q\t\tQuit Program\n\n");


        scanf("\n%c", &userInput);

        switch(userInput)
        {
            case 's':
                userSearch();
                break;
            case 'i':
                userInsert();
                break;
            case 'd':
                userDelete();
                break;
            case 'p':
                displayChain();
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
* Frees all memory allocated for the hash table links.
**/
void freeChain(void)
{
    int i;
    for (i = 0; i < size; i++)
    {
        if(hash_Table[i].next != NULL)
        {
            struct node* current = hash_Table[i].next;
            struct node* previous = NULL;

            while(current->next != NULL)
            {
                previous = current;
                current = current->next;

                free(previous);
            }
        }

    }
}


/**
* Main function to control building and manipulating the hash table.
**/
int main(int argc, char **argv)
{
    readCommandLine(argc, argv);

    if(helpFlag || argc == 1)
    {
        printf("Please run program in the following format:\n\n"
               "./hashtable -f DataSetn.txt\n\n");
        return 0;
    }

    double runtime;

    srand((unsigned long)time(NULL));

    a = getRandomNumber();
    b = getRandomNumber();

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

        fgets(singleLine, 100, fPointer);
        size = atoi(singleLine)*1.5; //size of the data set * 1.5 gives size of hashTable
        p = getPrimeNumber();

        initializeHashTable();

        while (fgets(singleLine, 100, fPointer)) //reads from fPointer till \n is found, stores everything in singleLine array
        {
            readOneLine(singleLine);
        }
        fclose(fPointer);

        end = clock();

        printf("\nHash Table built successfully!");

        runtime = (double)((end-start)/CLOCKS_PER_SEC);

        if(runtime == 0)
        {
            printf("\nRuntime to build table: shorter than .000001 seconds.\n");
        }
        else
        {
            printf("\nRuntime to build table in seconds: %f\n", runtime);
        }
    }

    userControl();

    freeChain();
    free(hash_Table);

    return 0;
}


