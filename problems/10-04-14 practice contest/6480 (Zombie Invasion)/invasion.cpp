#include <iostream>
using namespace std;

void test_case ()
{
	int const villageCount = 20;
	int population[villageCount];
	for (int i=0;i<villageCount;++i)
		cin >> population[i];

	for (int i=villageCount - 1;i>0;--i)
	{
		population[i-1] += population[i] / 2;
		population[i] = population[i] % 2;
	}

	for (int i=0;i<villageCount;++i)
	{
		cout << population[i];
		if (i != villageCount - 1)
			cout << " ";
	}
	cout << endl;
}

int main ()
{
	int n;
	cin >> n;
	for (int i=0;i<n;++i)
	{
		test_case ();
	}
}