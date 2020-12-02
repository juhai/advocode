from typing import List
from itertools import product


def get_result(numbers: List[int]):
    first_sums = sorted((a+b, a, b) for a, b in product(numbers, repeat=2) if a+b <= 2020)
    s_numbers = sorted(numbers)
    b = 0
    e = len(first_sums) - 1
    target = 2020
    while b < e:
        if s_numbers[b] + first_sums[e][0] > target:
            e -= 1
        elif s_numbers[b] + first_sums[e][0] < target:
            b += 1
        else:
            return s_numbers[b], first_sums[e][1], first_sums[e][2], s_numbers[b] * first_sums[e][1] * first_sums[e][2]


def main():
    with open('day1_data.txt') as i:
        numbers = [int(line.strip()) for line in i]
    result = get_result(numbers)
    print(f'Result is {result}')


if __name__ == '__main__':
    main()
