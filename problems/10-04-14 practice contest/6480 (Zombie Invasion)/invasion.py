#!/usr/bin/env python

num_cases = input()
for case_num in xrange(num_cases):
    counts = map(int, raw_input().split())
    for i in xrange(len(counts) - 1, 0, -1):
        carry, counts[i] = divmod(counts[i], 2)
        counts[i - 1] += carry
    print ' '.join(map(str, counts))