#!/usr/bin/env python
import collections
import sys

SOURCE_ID = 'Z'
SINK_ID = 'D'


def read_graph():
    height, width = map(int, raw_input().split())
    capacities = collections.defaultdict(lambda: collections.defaultdict(int))

    def edge(a, b, capacity=1):
        capacities[a][b] += capacity
        capacities[b][a] += capacity

    prev_line = None
    for y in xrange(height):
        line = raw_input()
        assert len(line) >= width
        for x in xrange(width):
            if line[x] == 'X':
                continue
            me = (x, y)
            if line[x] == 'D':
                edge(me, SINK_ID, 4)
            if y == 0:
                edge(me, SOURCE_ID)
            elif prev_line[x] in '.D':
                edge(me, (x, y - 1))
            if x == 0:
                edge(me, SOURCE_ID)
            elif line[x - 1] in '.D':
                edge(me, (x - 1, y))
            if x == width - 1:
                edge(me, SOURCE_ID)
            if y == height - 1:
                edge(me, SOURCE_ID)
        prev_line = line

    return capacities


# Based on http://en.wikibooks.org/wiki/Algorithm_implementation/Graphs/Maximum_flow/Edmonds-Karp
def max_flow(capacities):
    F = collections.defaultdict(lambda: collections.defaultdict(int))
    while True:
        parents = {SOURCE_ID: SOURCE_ID}
        path_capacity = {SOURCE_ID: sys.maxint}
        queue = collections.deque([SOURCE_ID])
        while queue:
            u = queue.popleft()
            for v, cap_u_v in capacities[u].iteritems():
                if cap_u_v - F[u][v] > 0 and v not in parents:
                    parents[v] = u
                    path_capacity[v] = min(path_capacity[u], cap_u_v - F[u][v])
                    if v != SINK_ID:
                        queue.append(v)
                    else:
                        while parents[v] != v:
                            u = parents[v]
                            F[u][v] += path_capacity[SINK_ID]
                            F[v][u] -= path_capacity[SINK_ID]
                            v = u
                        break
        if SINK_ID not in parents:
            return sum(F[SOURCE_ID].itervalues())


def main():
    num_cases = input()
    for case_num in xrange(num_cases):
        print max_flow(read_graph())

main()