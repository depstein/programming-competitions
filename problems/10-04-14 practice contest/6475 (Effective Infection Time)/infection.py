#!/usr/bin/env python
#-------------------------------------------------------------------------------
# Name:        andre.py
# Purpose:     Solve EIT problem
#
# Author(s):   Andre Wiggins
#
# Created:     January 20, 2013
#-------------------------------------------------------------------------------

n = input()
#print n

for case in xrange(n):
    start_month, start_year = map(int, raw_input().split(' '))
    end_month, end_year = map(int, raw_input().split(' '))

    #print start_month, start_year
    #print end_month, end_year

    value = 0
    if end_year > start_year:
        diff_years = end_year - start_year

        diff_years -= 1
        value += .5

        value += diff_years
        value += (end_month - 1) * (1/12.)

    elif end_year == start_year and end_month != start_month:
        diff_month = end_month - start_month
        value += diff_month * (.5/(12-start_month + 1))

    print "%.4f" % value