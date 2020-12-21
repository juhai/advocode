import itertools
import re
from dataclasses import dataclass
from typing import List, Tuple

instruction_re = re.compile(r'^mem\[(\d+)\] = (\d+)$')


@dataclass
class Program:
    mask: str
    one_mask: int
    zero_mask: int
    writes: List[Tuple[int, int]]

    def __str__(self):
        return (
            f'Program(mask={self.mask}, one_mask={bin(self.one_mask)}, ' +
            f'zero_mask={bin(self.zero_mask)}, writes={self.writes}'
        )


@dataclass
class MaskedWrite:
    address_with_mask: str
    value: int


def read_data(filename):
    programs = []
    current_writes: List[Tuple[int, int]] = []
    start = True
    mask = None
    with open(filename) as i:
        for line in i:
            if line.startswith('mask'):
                if start:
                    mask = line.strip().split()[-1]
                    start = False
                    continue
                programs.append(Program(
                    mask=mask, one_mask=get_one_mask(mask), zero_mask=get_zero_mask(mask), writes=current_writes
                ))
                mask = line.strip().split()[-1]
                current_writes = []
            if line.startswith('mem'):
                groups = instruction_re.match(line.strip()).groups()
                current_writes.append((int(groups[0]), int(groups[1])))
        programs.append(Program(
            mask=mask, one_mask=get_one_mask(mask), zero_mask=get_zero_mask(mask), writes=current_writes))
    return programs


def get_one_mask(mask: str) -> int:
    return int(''.join(['1' if b == '1' else '0' for b in list(mask)]), 2)  # Use OR


def get_zero_mask(mask: str) -> int:
    return int(''.join(['0' if b == '0' else '1' for b in list(mask)]), 2)  # Use AND


def main():
    programs = read_data('day_14_input.txt')
    memory = {}
    for program in programs:
        for instruction in program.writes:
            address = int(instruction[0])
            value = int(instruction[1])
            result = ((value & program.zero_mask) | program.one_mask)
            memory[address] = result

    print(f'Result is {sum(memory.values())}')


def merge_with_mask(address: int, mask: str) -> str:
    bin_address = f'{address:036b}'
    merged_with_mask = ''.join(
        'X' if m == 'X' else '1' if m == '1' else a for a, m in zip(bin_address, mask))
    return merged_with_mask


def main2():
    programs = read_data('day_14_input.txt')
    masked_writes = [
        MaskedWrite(
            address_with_mask=merge_with_mask(write[0] | program.one_mask, program.mask),
            value=write[1]
        )
        for program in programs for write in program.writes
    ]

    memory = {}
    for write in masked_writes:
        all_addresses = get_all_addresses(write.address_with_mask)
        for address in all_addresses:
            memory[address] = write.value

    print(f"Result is: {sum(memory.values())}")


def get_all_addresses(mask: str) -> List[int]:
    """Returns list of addresses that have the mask applied"""
    masked_addresses = []
    queue = [('', mask)]  # Prefix, mask
    while queue:
        done, remaining = queue.pop(0)
        if not remaining:  # no mask left
            masked_addresses.append(int(done, 2))
            continue
        if remaining[0] == 'X':
            queue.append((done + '1', remaining[1:]))
            queue.append((done + '0', remaining[1:]))
        else:
            queue.append((done + remaining[0], remaining[1:]))

    return masked_addresses


if __name__ == '__main__':
    main()
    main2()
