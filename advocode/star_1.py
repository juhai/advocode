from typing import List


def get_result(numbers: List[int]):
    s_numbers = sorted(numbers)
    b = 0
    e = len(s_numbers) - 1
    target = 2020
    while b < e:
        if s_numbers[b] + s_numbers[e] > target:
            e -= 1
        elif s_numbers[b] + s_numbers[e] < target:
            b += 1
        else:
            return s_numbers[b], s_numbers[e], s_numbers[b] * s_numbers[e]


def main():
    with open('day1_data.txt') as i:
        numbers = [int(line.strip()) for line in i]
    result = get_result(numbers)
    print(f'Result is {result}')


if __name__ == '__main__':
    main()
