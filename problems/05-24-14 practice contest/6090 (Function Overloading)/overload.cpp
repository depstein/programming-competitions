// ideas from G. V. Cormack, "An Algorithm for the Selection of
// Overloaded Functions in ADA

#include <iostream>
#include <algorithm>
#include <string>
#include <sstream>
#include <map>
#include <vector>
#include <cctype>
#include <cassert>

using namespace std;

struct Func
{
  vector<char> param;
  char ret;
  int id;

  Func() { };
  Func(istringstream &iss, int id)
  {
    int n;
    iss >> n;

    while (n--) {
      char p;
      iss >> p;
      param.push_back(p);
    }
    iss >> ret;
    this->id = id;
  }
};

vector<Func> table[256][10];

void read_decl()
{
  string line;
  int id = 1;
  char prev_name = ' ';
  while (getline(cin, line) && line != "#") {
    istringstream iss(line);
    char name;
    iss >> name;
    if (name != prev_name) {
      id = 1;
    }
    Func f(iss, id++);
    table[(unsigned)name][f.param.size()].push_back(f);
    prev_name = name;
  }
}

// indexed by position and type: ans[start,type]
map<pair<int,char>,int> ans;
map<int,int> len;

void comp_len(const string &line, int start)
{
  assert((unsigned)start < line.length());
  len[start] = 2;
  if (isupper(line[start])) {
    return;
  }

  int num_params = line[start+len[start]] - '0';
  len[start] += 2;
  for (int i = 0; i < num_params; i++) {
    int child_start = start + len[start];
    comp_len(line, child_start);
    len[start] += len[child_start];
  }
}

const int MAX_CHOICES = 1000+1;

// 0 for none, 1 if unique, 2 if ambiguous
int choices(const string &line, int start, char type,
	    map<pair<int,char>, Func *> &ids)
{
  assert((unsigned)start < line.length());

  if (ans.find(make_pair(start, type)) != ans.end()) {
    return ans[make_pair(start, type)];
  }

  char name = line[start];

  if (isupper(name)) {
    // just a type, terminal
    return ans[make_pair(start, type)] = (type == name);
  }

  int num_params = line[start+2] - '0';

  int sols = 0;
  vector<Func> &flist = table[(unsigned)name][num_params];
  int oldpos = start + 4;

  for (unsigned int i = 0; i < flist.size() && sols <= MAX_CHOICES-1; i++) {
    Func &f = flist[i];
    if (f.ret != type) continue;

    int comb = 1;
    int pos = oldpos;
    for (int j = 0; j < num_params && comb > 0; j++) {
      comb *= choices(line, pos, f.param[j], ids);
      comb = min(comb, MAX_CHOICES);
      pos += len[pos];
    }

    if (comb > 0) {
      ids[make_pair(start, type)] = &f;
    }
    
    sols += comb;
  }

  return ans[make_pair(start, type)] = min(sols, MAX_CHOICES);
}

void print(const string &line, int start, char type,
	   map<pair<int, char>, Func *> &ids)
{
  assert((unsigned)start < line.length());
  char name = line[start];

  if (isupper(name)) {
    cout << ' ' << name;
    return;
  }
  
  int num_params = line[start+2] - '0';
  //  cout << "   start, type = " << start << ", " << type << endl;
  assert(ids.find(make_pair(start, type)) != ids.end());
  Func *f = ids[make_pair(start, type)];
  cout << ' ' << name << f->id << ' ' << num_params;

  int pos = start+4;
  for (int j = 0; j < num_params; j++) {
    print(line, pos, f->param[j], ids);
    pos += len[pos];
  }
}

void solve(string line)
{
  line += ' ';         // so all types are terminated by a space to make
                       // parsing easier.
  
  char type = line[0];
  char eq = line[2];
  assert(eq == '=');

  len.clear();
  comp_len(line, 4);
  
  map<pair<int,char>, Func *> ids;
  ans.clear();

  int c = choices(line, 4, type, ids);
  assert(c != 1 || (unsigned)(len[4] + 4) == line.length());
  if (c == 0) {
    cout << "impossible" << endl;
  } else if (c > 1) {
    cout << "ambiguous ";
    if (c < MAX_CHOICES) {
      cout << c << endl;
    } else {
      cout << "> " << MAX_CHOICES-1 << endl;
    }
  } else {
    cout << type << " =";
    print(line, 4, type, ids);
    cout << endl;
  }
}

int main()
{
  read_decl();
  
  string line;
  while (getline(cin, line) && line != "#") {
    solve(line);
  }
  
  return 0;
}
