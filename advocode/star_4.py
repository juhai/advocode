from typing import List, Tuple


def get_result(passwords: List[Tuple[int, int, str, str]]):
    valid = 0
    for low_r, high_r, letter, password in passwords:
        valid += sum(1 for c in [password[low_r-1], password[high_r-1]] if c == letter) == 1
    return valid


def main():
    with open('day2_data.txt') as i:
        data = []
        for line in i:
            r, letter, password = line.strip().split()
            low_r = int(r.split('-')[0])
            high_r = int(r.split('-')[1])
            letter = letter.rstrip(':')
            data.append((low_r, high_r, letter, password))

    result = get_result(data)
    print(f'Result is {result}')


if __name__ == '__main__':
    main()
