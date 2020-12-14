import math
import sys
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
            print_map(released_seat_map)
            break
        finger_print = get_finger_print(released_seat_map)


def main2():
    pass


if __name__ == '__main__':
    main()
    main2()
