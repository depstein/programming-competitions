#!/usr/bin/python

# DONE

import re
import sys
import math
import pprint

def infect(grid, row, col):
    stack = [(row, col)]
    visited_outbreak = set()
    while stack:
        r, c = stack.pop()
        if grid[r][c] < 'D':   grid[r][c] = chr(ord(grid[r][c]) + 1)
        elif (r, c) not in visited_outbreak:
            visited_outbreak.add((r, c))
            for (nr, nc) in [(r-1,c), (r+1, c), (r,c-1), (r,c+1)]:
                if nr >= 0 and nr < len(grid) and nc >= 0 and nc < len(grid[0]) and \
                        grid[nr][nc] != 'X' and (nr, nc) not in visited_outbreak:
                    stack.append((nr, nc))

rl = sys.stdin.readline

numCases = int(rl())
assert 1 <= numCases <= 50

for _ in range(numCases):
    w, h = map(int, rl().split())
    assert 1 <= w <= 100
    assert 1 <= h <= 100

    grid = []
    for __ in range(h):
        line = rl().strip()
        assert len(line) == w
        assert re.match('^[ABCDX]+$', line)
        grid.append(list(line))

    numEvents = int(rl())
    for _ in range(numEvents):
        x, y = map(int, rl().split())
        assert 0 <= x < w
        assert 0 <= y < h

        infect(grid, y, x)

    print '\n'.join([''.join(row) for row in grid])

assert not rl()
