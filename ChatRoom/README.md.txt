# Project: Chat Room
## Student(s):  Tyler Fenske

## Introduction
This program's main purpose was to implement a Tuple Space from scratch. A basic implementation, 
as well as a more advanced implementation (using a trie data structure) are both implemented. 
To prove the functionality of the TupleSpace, a stress test that compares the performance of the
basic and trie tuple spaces was included. Finally, a basic, console-based ("fake") chat room was 
created on top of the trie tuple space.

## Usage
Both StressTest and ChatRoom have separate jar files that can be run from the
command line (with no command line arguments). 

### StressTest Usage
Simply run the jar with no command line arguments from the command line. The test may take several minutes
as the default creates and adds 100,000 tuples to each tuple space, then removes all of those tuples, printing
out the time taken for each operation on each tuple space. If desired, the global final variable TEST_COUNT may
be altered in the source code to reduce or increase the number of tuples tested. Also, the variable TUPLE_SPACE_SIZE
can be changed to change the value of k in the k-tuples produced for the test, where k is the max size a random
tuple can be generated with.

### ChatRoom Usage
The chat room is entirely console based, and should be run from the provided jar file by opening it on the console.
The chat room has two control settings: User, and System. 

The system can perform the following actions:
"/create"	- Create new user (and log them in).
"/logon"	- Log user onto the chat room.
"/pass"		- Pass control to first user (or return control to user that called "/system").
"/kill"		- Kill chat room program.

A user can perform the following actions:
"/printactive"	- Print all active users to console.
"/printall"		- Print all users to console.
"/logoff"		- Log off this user.
"/send"			- Send message to all users.
"/pass"			- Pass control to next user.
"/system"		- Pass control to System.

Both the system, and users, can type "/help" for the above actions to be listed while in the program.

The program starts in System mode, and will remain in system mode until at least one user has been created,
and "/pass" has been entered. Once you break out of the inital system mode, the program will loop over all 
active users. During a user's turn, they may call "/printactive", "/printall", "/system", and "/help" as many 
times as they like. Once the user calls "/logoff", "/send", or "/pass", their turn will be ended after the respective
command executes. Every time a user's turn ends, a copy of the chat room's message log will print to console. The
message log contains time stamps, user names, and messages for the 10 most recent messages written by any user, 
in chronological order. 

At anytime, a user may call "/system", passing control back to the system until "/pass" is once again called. If
all users become inactive, the program will go to and remain in system mode until a user becomes active, and "/pass" 
is called. 


## Jars 
The jar files are located in the root directory of the repository.

## Docs
The class diagram is located in the docs directory (which is located in the 
root directory of the repository). 

## Known Issues
No known bugs or issues.