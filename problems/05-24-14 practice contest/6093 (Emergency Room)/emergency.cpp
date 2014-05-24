#include <iostream>
#include <algorithm>
#include <queue>
#include <vector>
#include <cassert>

using namespace std;

struct Treatment
{
  int priority, time;
};

struct Doctor
{
  int time;
  Doctor(int t)
    : time(t) { }

  bool operator<(const Doctor &p) const
  {
    return time > p.time;
  }
};

struct Patient
{
  int arr, curr;
  unsigned int index;
  vector<Treatment> treatment;

  bool operator<(const Patient &p) const
  {
    assert(index < treatment.size() && p.index < p.treatment.size());
    if (treatment[index].priority != p.treatment[p.index].priority) {
      return treatment[index].priority < p.treatment[p.index].priority;
    } else {
      return arr > p.arr;
    }
  }

  void treat(Doctor &d)
  {
    assert(!done());
    curr = d.time + treatment[index++].time;
    d.time = curr;
  }
  
  bool done() const
  {
    return index >= treatment.size();
  }
};

struct currcmp
{
  bool operator()(const Patient &p1, const Patient &p2)
  {
    return p1.curr > p2.curr;
  }
};

bool outcmp(const Patient &p1, const Patient &p2)
{
  if (p1.curr != p2.curr) return p1.curr < p2.curr;
  return p1.arr < p2.arr;
}

istream &operator>>(istream &is, Treatment &t)
{
  return is >> t.priority >> t.time;
}

istream &operator>>(istream &is, Patient &p)
{
  is >> p.arr;
  p.index = 0;
  p.curr = p.arr;
  if (p.arr != -1) {
    p.treatment.clear();
    Treatment t;
    while (is >> t && (t.priority && t.time)) {
      p.treatment.push_back(t);
    }
  }
  return is;
}

ostream &operator<<(ostream &os, const Patient &p)
{
  return os << "Patient " << p.arr << " released at clock = " << p.curr;
}

bool solve(int id)
{
  int num_doctors, start_t;

  cin >> num_doctors >> start_t;
  if (!num_doctors && !start_t)
    return false;

  priority_queue<Doctor> doctors;
  for (int i = 0; i < num_doctors; i++) {
    doctors.push(Doctor(start_t));
  }
  
  priority_queue<Patient, vector<Patient>, currcmp> plist;
  Patient p;
  while (cin >> p && p.arr != -1) {
    plist.push(p);
  }

  priority_queue<Patient> waiting;
  vector<Patient> donelist;
  unsigned int rem = plist.size();
  while (rem > 0) {
    // take the first available doctor
    Doctor d = doctors.top();
    doctors.pop();

    // insert everyone on plist that has arrives before the doctor becomes
    // available
    while (!plist.empty() && plist.top().curr <= d.time) {
      waiting.push(plist.top());
      plist.pop();
    }

    // no one is waiting, so doctor can just wait for the first patient
    if (waiting.empty()) {
      assert(!plist.empty());
      d.time = plist.top().curr;
    } else {
      Patient p = waiting.top();
      waiting.pop();
      
      // treat patient
      p.treat(d);
      
      if (p.done()) {
	donelist.push_back(p);
	rem--;
      } else {
	plist.push(p);
      }
    }
    doctors.push(d);
  }

  sort(donelist.begin(), donelist.end(), outcmp);

  cout << "Case " << id << ":" << endl;
  for (unsigned int i = 0; i < donelist.size(); i++) {
    cout << donelist[i] << endl;
  }
  return true;
}

int main()
{
  int id = 1;
  while (solve(id++))
    ;

  return 0;
}
