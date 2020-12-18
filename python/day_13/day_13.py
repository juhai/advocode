import itertools
from dataclasses import dataclass

import contexttimer as contexttimer


@dataclass
class Bus:
    id: int
    delay: int


def main():
    with open('day_13_input.txt') as i:
        earliest_departure = int(i.readline())
        available_buses = [int(bus) for bus in i.readline().split(',') if bus != 'x']

    dep_after_earliest = [bus - earliest_departure % bus for bus in available_buses]
    earliest_bus = min(enumerate(dep_after_earliest), key=lambda item: item[1])

    print(f'Result is {available_buses[earliest_bus[0]] * earliest_bus[1]}')


def main2():
    with open('day_13_input.txt') as i:
        i.readline()
        all_buses = i.readline().strip().split(',')
    buses = [
        Bus(id=bus, delay=delay) for bus, delay in zip(
            [int(bus) for bus in all_buses if bus != 'x'],
            [delay for delay, bus in enumerate(all_buses) if bus != 'x']
        )
    ]

    with contexttimer.Timer() as t:
        seq = itertools.count(0, buses[0].id)
        for bus in buses[1:]:
            seq = get_new_seq(bus.id, bus.delay, seq)
        print(f"You need to wait for {next(seq)} minutes, but calculation took only {t.elapsed} seconds")


def get_new_seq(bus_id, bus_delay, seq):
    new_seq = filter(lambda s: (s + bus_delay) % bus_id == 0, seq)
    first_value = next(new_seq)
    second_value = next(new_seq)
    return itertools.count(start=first_value, step=second_value-first_value)


if __name__ == '__main__':
    main()
    main2()
