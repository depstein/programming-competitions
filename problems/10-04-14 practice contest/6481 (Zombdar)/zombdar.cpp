#include <iostream>
using namespace std;


void test_case ()
{
	string buffer;
	string line;
	int elapsed = 0;
	while (getline (cin, line))
	{
		if (line == "END OF CASE")
			break;
		elapsed++;
		for (int i=0;i<line.size ();++i)
		{
			buffer += line[i];
			if (line[i] == ';')
			{
				cout << elapsed << ": " << buffer << endl;
				buffer = "";
			}
		}
	}

}

int main ()
{
	int num;
	string newLine;
	cin >> num;
	// Eat new line character after test case count
	getline (cin, newLine);
	for (int i = 0; i < num; ++i)
	{
		test_case ();
	}
}