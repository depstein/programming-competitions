// fun.cpp
// Fun House, MCPC 2014, Problem B
// C++ solution by James Feher

#include <fstream>
#include <iostream>
#include <cstring>
using namespace std;

ifstream fin( "fun.in" );
bool DEBUG = false;      // Make sure to set to false on submission
int W, L;
char room[21][21];

int findEntrance(int &inX, int&inY) {
// set the location of the input to the room
// return the direction of the beam
// 0 - right, 1 - left, 2 - up, 3 - down
  int i, direction;
  bool found = false;

  // check the top && bottom rows
  for (i=0; i<W && !found; i++) {
    if (room[0][i] == '*') {
      found     = true;
      inX       = i;
      inY       = 0;
      direction = 3;    // at top row, so go down
    }
    if (room[W-1][i] == '*') {
      found     = true;
      inX       = i;
      inY       = W-1;
      direction = 2;    // on bottom so go up
    }
  }
  // check the left and right sides
  for (i=0; i<L && !found; i++) {
    if (room[i][0] == '*') {
      found     = true;
      inX       = 0;
      inY       = i;
      direction = 0;    // on left so go right
    }
    if (room[i][L-1] == '*') {
      found     = true;
      inX       = L-1;
      inY       = i;
      direction = 1;    // on right so go left
    }
  }
  
  return direction;  
} // end findEntrance

bool getNextPoint(int direction, int &X, int &Y) {
  bool done = false;
  if (direction == 0)           // right
    X++;
  else if (direction == 1)      // left
    X--;
  else if (direction == 2)      // up
    Y--;
  else if (direction == 3)      // down
    Y++;
  else
    cout << "ERROR: direction never less than 0 greater than 3.\n";

  // see if we are at a wall now and should stop
  if (room[Y][X] == 'x') 
    done = true;    
  return done;
} // end getNextPoint

int main() {   
  int i, j, X, Y, inX, inY, direction, houseNum = 1;
  // inX - input x coordinate
  // inY - input y coordinate
  // direction : 0 - right, 1 - left, 2 - up, 3 - down
  bool done;

  fin >> W;             // Get the width of the room

  while (W > 0) {
    fin >> L;
    DEBUG && cout << "Got Data -- W: " << W << "\tL: " << L <<  endl;
    
    // get each row in the room array of chars
    for (i=0; i<L; i++)
      fin >> room[i];   

    if (DEBUG) {
      cout << "INPUT ROOM: \n";
      for (i=0; i<L; i++) 
        cout << room[i] << endl;
    }

    direction = findEntrance(inX, inY);
    X = inX;
    Y = inY;
    done = false;
    int count = 0, maxCount = 2*W*L;    
    // maxCount to short circuit misprogrammed loops and terminate anything
    // that might be going longer than it needs to. Could hit both sides of 
    // mirrors so it should never be greater than 2*(W-2)*(L-2)
    while (!done && count < maxCount) {
      count++;
      // get the next spot for the light beam to go
      done = getNextPoint(direction, X, Y);
      DEBUG && cout << "count: " << count << "\tdirection: " << direction 
                    << "\tX: " << X << "\tY: " << Y << "\troom" << room[Y][X] << endl;

      if (!done) {
        if (room[Y][X] == '/') {
          // right goes up 0->2, left goes down 1->3
          // up goes right 2->0, down goes left 3->1
          direction = (direction + 2) % 4;
        }
        if (room[Y][X] == '\\') {       // Escape the \\
          // right goes down 0->3, left goes up 1->2
          // down goes right 3->0, up goes left 2->1
          if (direction == 0) 
            direction = 3;
          else if (direction == 3) 
            direction = 0;
          else if (direction == 1) 
            direction = 2;
          else if (direction == 2) 
            direction = 1;
        }  
      }
    }
    room[Y][X] = '&';

    cout << "HOUSE " << houseNum << endl;
    for (i=0; i<L; i++) 
      cout << room[i] << endl;

    fin >> W;
    houseNum++;
  }
  return 0;
}
