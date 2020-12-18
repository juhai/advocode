import math
import sys
from itertools import takewhile, dropwhile
from typing import List, Dict, Tuple


def make_map(lines: List[str]) -> Dict[Tuple[int, int], str]:
    seat_map = {}
    for idx, row in enumerate(lines):
        for cidx, c in enumerate(row.strip()):
            seat_map[(idx, cidx)] = c
    return seat_map


def reserved_seat_neighbour_count(seat_map: Dict[Tuple[int, int], str], seat_loc: Tuple[int, int]) -> int:
    return sum(
        1 for r_idx in range(seat_loc[0]-1, seat_loc[0]+2)
        for c_idx in range(seat_loc[1]-1, seat_loc[1]+2)
        if (r_idx, c_idx) != seat_loc and seat_map.get((r_idx, c_idx), '') == '#'
    )


def print_map(seat_map: Dict[Tuple[int, int], str]):
    grid_size = int(math.sqrt(len(seat_map)) + 0.5)
    print('Seat map')
    for row in range(grid_size):
        for col in range(grid_size):
            sys.stdout.write(f'{seat_map[(row, col)]}')
        print()


def print_map_list(seat_map_list: List[str]):
    grid_size = int(math.sqrt(len(seat_map_list)) + 0.5)
    print('Seat map')
    for row in range(grid_size):
        for col in range(grid_size):
            sys.stdout.write(f'{seat_map_list[row*grid_size + col]}')
        print()


def get_finger_print(seat_map: Dict[Tuple[int, int], str]) -> str:
    return ''.join(seat_map.values())


def get_num_occupied(seat_map: Dict[Tuple[int, int], str]) -> int:
    return sum(1 for c in seat_map.values() if c == '#')


def main():
    with open('day_11_input.txt') as i:
        seat_map = make_map(i.readlines())
    print(reserved_seat_neighbour_count(seat_map, (0, 0)))
    finger_print = get_finger_print(seat_map)
    released_seat_map = seat_map.copy()
    while True:
        # Occupy
        occupied_seat_map = {}
        for (r, c), value in released_seat_map.items():
            occupied_seat_map[(r, c)] = '#' if value != '.' and reserved_seat_neighbour_count(released_seat_map, (r, c)) == 0 else value
        # print_map(occupied_seat_map)

        # Release
        released_seat_map = {}
        for (r, c), value in occupied_seat_map.items():
            released_seat_map[(r, c)] = (
                'L' if value != '.' and reserved_seat_neighbour_count(occupied_seat_map, (r, c)) >= 4 else value)
        # print_map(released_seat_map)
        if get_finger_print(released_seat_map) == finger_print:
            print(f'Done {get_num_occupied(released_seat_map)}')
            # print_map(released_seat_map)
            break
        finger_print = get_finger_print(released_seat_map)


def get_view(seat_map_list: List[str], direction: str, pos: int, grid_size: int) -> List[str]:
    num_seats = len(seat_map_list)
    row = pos // grid_size
    if direction == 'right':
        return seat_map_list[pos+1:(row+1)*grid_size]
    if direction == 'left':
        return seat_map_list[row*grid_size:pos][::-1]
    if direction == 'up':
        sequence = range(pos-grid_size, -1, -grid_size)
        return [seat_map_list[n] for n in sequence]
    if direction == 'down':
        return seat_map_list[pos+grid_size:num_seats:grid_size]
    if direction == 'down-right':
        sequence = range(pos, num_seats, grid_size+1)
        return [seat_map_list[n] for p, n in takewhile(lambda p: p[0] % grid_size + 1 == p[1] % grid_size, zip(sequence, sequence[1:]))]
    if direction == 'down-left':
        sequence = range(pos, num_seats, grid_size-1)
        return [seat_map_list[n] for p, n in takewhile(lambda p: p[1] % grid_size + 1 == p[0] % grid_size, zip(sequence, sequence[1:]))]
    if direction == 'up-right':
        sequence = range(pos, -1, -(grid_size-1))
        return [seat_map_list[n] for p, n in takewhile(lambda p: p[0] % grid_size + 1 == p[1] % grid_size, zip(sequence, sequence[1:]))]
    if direction == 'up-left':
        sequence = range(pos, -1, -(grid_size+1))
        return [seat_map_list[n] for p, n in takewhile(lambda p: p[1] % grid_size + 1 == p[0] % grid_size, zip(sequence, sequence[1:]))]


# def test_get_view():
#     test_data = list(range(100))
#     assert get_view(test_data, 'up-left', 99, grid_size=10) == [88, 77, 66, 55, 44, 33, 22, 11, 0]
#     assert get_view(test_data, 'up-left', 23, grid_size=10) == [12, 1]
#     assert get_view(test_data, 'up-left', 3, grid_size=10) == []
#     assert get_view(test_data, 'up-left', 20, grid_size=10) == []
#
#     assert get_view(test_data, 'up-right', 90, grid_size=10) == [81, 72, 63, 54, 45, 36, 27, 18, 9]
#     assert get_view(test_data, 'up-right', 23, grid_size=10) == [14, 5]
#     assert get_view(test_data, 'up-right', 29, grid_size=10) == []
#     assert get_view(test_data, 'up-right', 10, grid_size=10) == [1]
#     assert get_view(test_data, 'up-right', 3, grid_size=10) == []
#
#     assert get_view(test_data, 'down-left', 9, grid_size=10) == [18, 27, 36, 45, 54, 63, 72, 81, 90]
#     assert get_view(test_data, 'down-left', 0, grid_size=10) == []
#     assert get_view(test_data, 'down-left', 23, grid_size=10) == [32, 41, 50]
#     assert get_view(test_data, 'down-left', 93, grid_size=10) == []
#     assert get_view(test_data, 'down-left', 20, grid_size=10) == []
#
#     assert get_view(test_data, 'down-right', 0, grid_size=10) == [11, 22, 33, 44, 55, 66, 77, 88, 99]
#     assert get_view(test_data, 'down-right', 23, grid_size=10) == list(range(34, 100, 11))
#     assert get_view(test_data, 'down-right', 93, grid_size=10) == []
#     assert get_view(test_data, 'down-right', 9, grid_size=10) == []
#     assert get_view(test_data, 'down-right', 90, grid_size=10) == []
#
#     assert get_view(test_data, 'right', 20, grid_size=10) == list(range(21, 30))
#     assert get_view(test_data, 'right', 23, grid_size=10) == list(range(24, 30))
#     assert get_view(test_data, 'right', 20, grid_size=10) == list(range(21, 30))
#     assert get_view(test_data, 'right', 29, grid_size=10) == []
#     assert get_view(test_data, 'right', 99, grid_size=10) == []
#
#     assert get_view(test_data, 'left', 0, grid_size=10) == []
#     assert get_view(test_data, 'left', 2, grid_size=10) == [1, 0]
#     assert get_view(test_data, 'left', 23, grid_size=10) == [22, 21, 20]
#     assert get_view(test_data, 'left', 20, grid_size=10) == []
#     assert get_view(test_data, 'left', 29, grid_size=10) == [28, 27, 26, 25, 24, 23, 22, 21, 20]
#     assert get_view(test_data, 'left', 9, grid_size=10) == [8, 7, 6, 5, 4, 3, 2, 1, 0]
#
#     assert get_view(test_data, 'up', 23, grid_size=10) == list(range(23-10, 0, -10))
#     assert get_view(test_data, 'up', 93, grid_size=10) == list(range(93-10, 0, -10))
#     assert get_view(test_data, 'up', 90, grid_size=10) == [80, 70, 60, 50, 40, 30, 20, 10, 0]
#     assert get_view(test_data, 'up', 3, grid_size=10) == []
#     assert get_view(test_data, 'up', 0, grid_size=10) == []
#
#     assert get_view(test_data, 'down', 0, grid_size=10) == list(range(10, 100, 10))
#     assert get_view(test_data, 'down', 9, grid_size=10) == list(range(19, 100, 10))
#     assert get_view(test_data, 'down', 23, grid_size=10) == list(range(33, 100, 10))
#     assert get_view(test_data, 'down', 93, grid_size=10) == []
#     assert get_view(test_data, 'down', 99, grid_size=10) == []
#     assert get_view(test_data, 'down', 3, grid_size=10) == list(range(13, 100, 10))


def get_finger_print2(seat_map_list: List[str]) -> str:
    return ''.join(seat_map_list)


directions = ['up', 'down', 'left', 'right', 'up-left', 'up-right', 'down-left', 'down-right']


def get_first_seat(seat_map_list: List[str], direction: str, pos: int, grid_size: int) -> str:
    seq = get_view(seat_map_list, direction, pos, grid_size)
    try:
        return next(dropwhile(lambda item: item == '.', seq))
    except StopIteration:
        return ''


def main2():
    with open('day_11_input.txt') as i:
        seat_map_list = list(i.read().replace('\n', ''))

    grid_size = 98

    print(len(seat_map_list), seat_map_list)
    finger_print = get_finger_print2(seat_map_list)
    released_seat_map_list = seat_map_list.copy()
    while True:
        # Occupy
        occupied_seat_map_list = []
        for idx, value in enumerate(released_seat_map_list):
            if value == '.' or value == '#':
                occupied_seat_map_list.append(value)
                continue
            reserved_count = 0
            for direction in directions:
                if get_first_seat(released_seat_map_list, direction, idx, grid_size) == '#':
                    reserved_count += 1
                    break
            if reserved_count == 0:
                occupied_seat_map_list.append('#')
            else:
                occupied_seat_map_list.append(value)
        # print_map_list(occupied_seat_map_list)

        # Release
        released_seat_map_list = []
        for idx, value in enumerate(occupied_seat_map_list):
            if value == '.' or value == 'L':
                released_seat_map_list.append(value)
                continue

            reserved_count = 0
            for direction in directions:
                first_seat = get_first_seat(occupied_seat_map_list, direction, idx, grid_size)
                if first_seat == '#':
                    reserved_count += 1
                    if reserved_count >= 5:
                        break
            if reserved_count >= 5:
                released_seat_map_list.append('L')
            else:
                released_seat_map_list.append('#')
        # print_map_list(released_seat_map_list)
        if get_finger_print2(released_seat_map_list) == finger_print:
            print(f'Done {released_seat_map_list.count("#")}')
            # print_map_list(released_seat_map_list)
            break
        finger_print = get_finger_print2(released_seat_map_list)


if __name__ == '__main__':
    main()
    main2()

