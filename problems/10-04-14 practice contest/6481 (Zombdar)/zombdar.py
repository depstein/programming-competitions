#!/usr/bin/env python
import sys

num_cases = input()
for case_num in xrange(num_cases):
    buf = ''
    for i, line in enumerate(sys.stdin):
        line = line.rstrip('\r\n')
        if line == 'END OF CASE':
            break
        pieces = (buf + line).split(';')
        buf = pieces.pop()
        for chunk in pieces:
            print '%d: %s;' % (i + 1, chunk)