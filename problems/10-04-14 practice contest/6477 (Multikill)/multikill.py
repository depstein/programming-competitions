#!/usr/bin/python

# DONE

import re
import sys
import math
import pprint

rl = sys.stdin.readline

numCases = int(rl())

def dist(a, b): return math.sqrt((a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2)

def count(zombies, radius, loc):
    numKilled = 0
    for z in zombies:
        if dist(z, loc) <= radius:
            numKilled += 1
    return numKilled

for _ in range(numCases):
    radius, n = rl().split()
    n = int(n)
    radius = float(radius)

    zombies = []
    for _ in range(n):
        x, y = map(float, rl().split())
        zombies.append((x, y))

    possible = zombies[:]
    for i in range(n):
        for j in range(i + 1, n):
            if dist(zombies[i], zombies[j]) > 2 * radius or zombies[i] == zombies[j]:
                continue

            x1, y1 = zombies[i]
            x2, y2 = zombies[j]

            p1 = q1 = p2 = q2 = None
            if x1 == x2:
                q1 = q2 = (y1 + y2) / 2.0
                dy = (y1 - y2) / 2.0
                dx = math.sqrt(radius * radius - dy * dy)
                p1 = x1 - dx
                p2 = x1 + dx
            else:
                # Derivation:
                #   (x - p)^2 + (y - q)^2 = r^2
                #   xx - 2px + pp + yy - 2qy + qq = rr
                #
                #   x1x1 - 2px1 + pp + y1y1 - 2qy1 + qq = rr
                #   x2x2 - 2px2 + pp + y2y2 - 2qy2 + qq = rr
                #
                #   x1x1 - 2px1 + pp + y1y1 - 2qy1 + qq - (x2x2 - 2px2 + pp + y2y2 - 2qy2 + qq) = 0
                #   x1x1 - x2x2 - 2px1 + y1y1 - 2qy1 + 2px2 - y2y2 + 2qy2 = 0
                #   p(2x2 - 2x1) + q(2y2 - 2y1) + x1x1 - x2x2 + y1y1 - y2y2 = 0
                #
                #   p = (q(2y1 - 2y2) + x2x2 - x1x1 + y2y2 - y1y1) / (2x2 - 2x1)
                # now we can write p in terms of q:
                #   p = a*q + b
                a = (y1 - y2) / (x2 - x1)
                b = (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1) / (2 * (x2 - x1))

                # revisit the formula for the circle involving only q now, and
                # solve the quadratic formula for the two values of q
                # (x - (aq + b))^2 + (y - q)^2 = r^2
                ap = a * a + 1
                bp = 2 * (a * b - a * x1 - y1)
                cp = x1 * x1 - 2 * x1 * b + b * b + y1 * y1 - radius * radius

                z = bp * bp - 4 * ap * cp
                q1 = (-bp + math.sqrt(z)) / (2 * ap)
                q2 = (-bp - math.sqrt(z)) / (2 * ap)
                p1 = q1 * a + b
                p2 = q2 * a + b

            if p1 is not None:
                possible.append((p1, q1))
                possible.append((p2, q2))

    maxKilled = [(count(zombies, radius, loc),) + loc for loc in possible]

    if maxKilled:
        print max(maxKilled)[0]
    else:
        print 0

assert not rl()
