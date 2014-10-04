#include <iostream>

#include <vector>
using namespace std;

bool twelveNumberProblem (vector<int> twelveSet_)
{
	for (int i = 0; i < 9; ++i)
	{
		// While this number exists
		while (twelveSet_[i])
		{
			// Find triples
			if (twelveSet_[i] >= 3)
				twelveSet_[i] -= 3;

			// Find straights
			// Can only find straights for 7 and less
			else if (i < 7 &&
			         twelveSet_[i + 1] && twelveSet_[i + 2])
			{
				--twelveSet_[i];
				--twelveSet_[i + 1];
				--twelveSet_[i + 2];
			}
			else
				// Couldn't find a triple or run
				return false;
		}
	}

	// Got through and used all numbers
	return true;
}

void testCase ()
{
	// Declare with 9 zero-initialized elements
	vector<int> set (9);

	// Read input
	for (int i = 0; i < 14; ++i)
	{
		int number;

		cin >> number;

		++set[number - 1];
	}

	bool success = false;

	// Find all pairs
	for (int i = 0; i < 9 && ! success; ++i)
	{
		if (set[i] >= 2)
		{
			vector<int> twelveSet = set;

			twelveSet[i] -= 2;

			success |= twelveNumberProblem (twelveSet);
		}
	}

	if (success)
		cout << "Vulnerable" << endl;
	else
		cout << "Immune" << endl;

}

int main ()
{
	int count;

	cin >> count;

	for (int i = 0; i < count; ++i)
		testCase ();
}
