#!/usr/bin/env python
NUM_DIGITS = 9


def is_vulnerable(counts):
    for pair_idx, pair_value in enumerate(counts):
        if pair_value >= 2:
            remain = list(counts)
            remain[pair_idx] -= 2
            for i, count in enumerate(remain):
                if count > 0 and count < 3:
                    if i < (NUM_DIGITS - 2) and remain[i + 1] >= count and remain[i + 2] >= count:
                        remain[i + 1] -= count
                        remain[i + 2] -= count
                    else:
                        break
            else:
                return True
    return False


def main():
    num_cases = input()
    for case_num in xrange(num_cases):
        counts = [0] * NUM_DIGITS
        for gene in raw_input().split():
            counts[int(gene) - 1] += 1
        if is_vulnerable(counts):
            print 'Vulnerable'
        else:
            print 'Immune'

main()