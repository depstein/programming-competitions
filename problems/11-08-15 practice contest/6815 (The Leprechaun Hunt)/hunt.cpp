// hunt.cpp
// The Leprechaun Hunt, MCPC 2014, Problem D
// C++ version by Michael Goldwasser

/*
  This implementation uses backward reasoning, with caching to detect
  known states.  It begins by examining all possible states in which
  the villagers have caught the leprechaun (i.e. villagers win in 0
  plies). Then it finds all states in which all of the leprechauns
  moves lead to a final state (i.e., villagers win in 1 plies), then
  all states in which the villager can reach such a state (i.e.,
  villagers win in 2 plies), and so on, until finding the best winning
  start state, or until exhausting all possibilities.
 */

#include <iostream>
#include <fstream>
#include <set>
#include <map>
#include <vector>
using namespace std;

#define MAX_N 15
#define MAX_E 45
#define MAX_DEG 6
#define MAX_V 7
#define LOG_N 4   // number of bits needed to encode node

int DEBUG = 0;

// store input information globally
int V;  // number of villagers
int N;  // number of nodes
int E;  // number of edges
vector<vector<int> > adj;   // adj[k] is a vector of k's neighbors

typedef long long StateCode;

struct State {
    bool villagerTurn;
    int leprechaun;
    set<int> villagers;

    State() { }

    // produce integer encoding for state
    const static StateCode MASK = (1L << LOG_N) - 1;
    StateCode code() const {
        // Encode game state. Least significant bit denotes turn (1=villagers).
        // Next LOG_N bits denote leprechaun's node; then LOG_N bits per villager.
        StateCode s = villagerTurn;
        s |= (leprechaun << 1);
        int offset = 1+LOG_N;
        for (set<int>::const_iterator it = villagers.begin();
             it != villagers.end(); ++it) {
            s |= ( ((StateCode) *it) << offset);
            offset += LOG_N;
        }
        return s;
    }

    // reconstruct state from code
    State(StateCode c) {
        villagerTurn = (c % 2 == 1);
        leprechaun = (c >> 1) & MASK;
        for (int j=0; j<V; j++)
            villagers.insert((c >> (1+LOG_N*(1+j))) & MASK);

        if (code() != c) {
            cerr << "INVALID RECONSTRUCTION" << endl;
            cerr << c << endl;
            cerr << code() << endl;
            cerr << toString() << endl;
        }
    }

    vector<State> neighbors() const {
        vector<State> nbrs;
        State copy(*this);
        copy.villagerTurn = !villagerTurn;
        if (villagerTurn) {
            for (set<int>::const_iterator it = villagers.begin();
                 it != villagers.end(); ++it) {
                int cur = *it;                                        // consider moving villager *it.
                copy.villagers.erase(cur);
                for (int j=0; j < adj[cur].size(); j++) {
                    if (copy.villagers.insert(adj[cur][j]).second) {  // not already occupied
                        nbrs.push_back(copy);
                        copy.villagers.erase(adj[cur][j]);
                    }
                }
                copy.villagers.insert(cur);
            }
        } else {  // leprechaun turn
            if (leprechaun != N)
                nbrs.push_back(copy);  // allowed to stay put (except on fake first move)
            for (int j=0; j < adj[leprechaun].size(); j++) {
                if (copy.villagers.count(adj[leprechaun][j]) == 0) {
                    copy.leprechaun = adj[leprechaun][j];
                    nbrs.push_back(copy);
                }
            }
        }
        return nbrs;
    }

    string toString() const {
        string result;
        result += ((char) ('A' + leprechaun));
        result += ',';
        for (set<int>::const_iterator it = villagers.begin();
             it != villagers.end(); ++it) {
            result += ((char) ('A' + *it));
        }
        result += " (";
        result += (villagerTurn ? "villager" : "leprechaun");
        result += " to move)";
        return result;
    }
};

// Recursively find all (N choose V) placements of villages on the graph
void generateVillagerPlacements(vector<State>& result, State& model) {
    if (model.villagers.size() == V) {
        result.push_back(model);
    } else {
        int next = (model.villagers.empty() ? 0 : 1 + *(model.villagers.rbegin()));
        for (int j=next; j < N; j++) {
            model.villagers.insert(j);
            generateVillagerPlacements(result, model);
            model.villagers.erase(j);
        }
    }
}

map<StateCode,int> win;    // win[s] = k means that villagers win after k plies from s

// return true if every leprechaun move from s leads to known state with wen < plies
bool trapped(const State& s, int plies) {
    if (DEBUG > 2)
        cerr << "  testing trapped for " << s.toString() << " " << plies << endl;
    vector<State> options = s.neighbors();
    for (int j=0; j < options.size(); j++) {
        StateCode code = options[j].code();
        if (win.find(code) == win.end() || win[code] >= plies)
            return false;
    }
    if (DEBUG > 2)
        cerr << "   TRAPPED is true" << endl;
    return true;
}

int analyze() {
    // start by finding potential win[0] candidates
    set<StateCode> fringe;
    vector<State> placements;
    State model;
    model.villagerTurn = false;
    model.leprechaun = 0;
    generateVillagerPlacements(placements, model);
    for (int j=0; j < placements.size(); j++) {
        State& s(placements[j]);
        for (set<int>::const_iterator it = s.villagers.begin();
             it != s.villagers.end(); ++it) {
            s.leprechaun = *it;
            StateCode code = s.code();
            win[code] = 0;
            fringe.insert(code);
        }
    }

    int plies = 0;  
    while (!fringe.empty()) {
        plies++;                 // everything in fringe has win < plies
        set<StateCode> newFringe;
        for (set<StateCode>::const_iterator it = fringe.begin(); it != fringe.end(); ++it) {
            State s(*it);  // reconstruct state from code

            if (DEBUG > 1)
                cerr << "win = " << win[*it] << " for " << s.toString() << endl;

            s.villagerTurn = !s.villagerTurn;  // FAKE to analyze reverse moves
            if (s.villagerTurn) {
                // consider villagers' previous move
                vector<State> nbrs = s.neighbors();
                for (int j=0; j < nbrs.size(); j++) {
                    nbrs[j].villagerTurn = true;
                    StateCode code = nbrs[j].code();
                    if (DEBUG > 3) cerr << "  Considering " << nbrs[j].toString() << endl;
                    if (win.find(code) == win.end()) {
                        if (DEBUG > 2) cerr << "  Adding to fringe " << nbrs[j].toString() << endl;
                        win[code] = plies;
                        newFringe.insert(code);
                    }
                }
            } else {
                // consider leprechaun's previous move (including possible N)
                State special(s);
                special.leprechaun = N;
                if (DEBUG > 2) cerr << "  Considering special " << special.toString() << endl;
                if (trapped(special, plies)) {
                    if (DEBUG > 0) cerr << "Winning initial state " << special.toString() << endl;
                    return plies-1;         // found a winning initial state!!!!
                }
                vector<State> nbrs = s.neighbors();
                for (int j=0; j < nbrs.size(); j++) {
                    nbrs[j].villagerTurn = false;
                    StateCode code = nbrs[j].code();
                    if (DEBUG > 3) cerr << "  Considering " << nbrs[j].toString() << endl;
                    if (win.find(code) == win.end())
                        if (trapped(nbrs[j], plies)) {
                            if (DEBUG > 2) cerr << "  Adding to fringe " << nbrs[j].toString() << endl;
                            win[code] = plies;
                            newFringe.insert(code);
                        }
                }
            }
        }
        fringe.swap(newFringe);
    }

    return -1;  // if fringe became empty, no winning strategy exists
}

int main(int argv, char** argc) {
    ifstream fin((argv == 1) ? "hunt.in" : argc[1]);
    if (argv > 2) DEBUG = atoi(argc[2]);
    int trial=0;
    while (true) {
        ++trial;
        fin >> V;
        if (V == 0) break;
        fin >> N >> E;
        adj.clear();
        adj.resize(1+N);
        for (int j=0; j < E; j++) {
            string edge;
            fin >> edge;
            int u(edge[0]-'A'), v(edge[1]-'A');
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        // fictitious node N is used as sentinel start node for leprechaun
        for (int u=0; u<N; u++)
            adj[N].push_back(u);

        win.clear();
        int plies = analyze();
        if (DEBUG > 0) cerr << "Solved " << win.size() << " states" << endl;
        cout << "CASE " << trial << ": ";
        if (plies == -1)
            cout << "NEVER" << endl;
        else
            cout << (1+plies)/2 << endl;   // count rounds, not plies
    }
}
