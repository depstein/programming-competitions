#!/usr/bin/env python
import collections
import sys


def shortest_path(edges, start_node, start_cost):
    path_costs = {start_node: start_cost}
    queue = collections.deque([start_node])
    while queue:
        node = queue.popleft()
        for neighbor, edge_cost in edges.get(node, {}).iteritems():
            new_cost = path_costs[node] + edge_cost
            if new_cost < path_costs.get(neighbor, sys.maxint):
                path_costs[neighbor] = new_cost
                queue.append(neighbor)
    return path_costs


def solve_case():
    num_outposts, num_roads = map(int, raw_input().split())

    start_id = None
    start_ammo = None
    extra_ammo_id = None
    extra_ammo = None
    supply_id = 'supply'
    roads = {}

    for i in xrange(num_outposts):
        callsign, ammo_str, supp_str = raw_input().split()
        ammo = int(ammo_str)

        if i == 0:
            start_id = callsign
            start_ammo = ammo
        if supp_str == 'yes':
            roads.setdefault(callsign, {})[supply_id] = 0
        elif i and ammo:
            extra_ammo_id = callsign
            extra_ammo = ammo

    for i in xrange(num_roads):
        a, b, z = raw_input().split()
        roads.setdefault(a, {})[b] = int(z)
        roads.setdefault(b, {})[a] = int(z)

    # Attempt to find a safe path to a supply outpost without picking up extra ammo.
    from_start_costs = shortest_path(roads, start_id, 0)
    target_cost = from_start_costs.get(supply_id, sys.maxint)
    if target_cost <= start_ammo:
        print target_cost
        return

    # Attempt to find a safe path to an outpost with extra ammo.
    if from_start_costs.get(extra_ammo_id, sys.maxint) <= start_ammo:
        # Attempt to find a safe path from there to a supply outpost.
        from_ammo_costs = shortest_path(roads, extra_ammo_id, from_start_costs[extra_ammo_id])
        target_cost = from_ammo_costs.get(supply_id, sys.maxint)
        if target_cost <= start_ammo + extra_ammo:
            print target_cost
            return

    print 'No safe path'


def main():
    num_cases = input()
    for case_num in xrange(num_cases):
        solve_case()

main()