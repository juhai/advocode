from dataclasses import dataclass
from functools import reduce
from typing import Tuple


@dataclass
class ShipStatus:
    bearing: int
    longitude: int
    latitude: int


@dataclass
class WayPointLocation:
    longitude: int
    latitude: int


BEARING_TO_DIRECTION = {
    0: 'E',
    90: 'S',
    180: 'W',
    270: 'N'
}


def apply_instruction(current_status: ShipStatus, command: str, amount: int) -> ShipStatus:
    if command == 'N':
        return ShipStatus(
            bearing=current_status.bearing, longitude=current_status.longitude, latitude=current_status.latitude+amount)
    if command == 'S':
        return ShipStatus(
            bearing=current_status.bearing, longitude=current_status.longitude, latitude=current_status.latitude-amount)
    if command == 'E':
        return ShipStatus(
            bearing=current_status.bearing, longitude=current_status.longitude+amount, latitude=current_status.latitude)
    if command == 'W':
        return ShipStatus(
            bearing=current_status.bearing, longitude=current_status.longitude-amount, latitude=current_status.latitude)
    if command == 'L':
        return ShipStatus(
            bearing=(current_status.bearing-amount) % 360,
            longitude=current_status.longitude,
            latitude=current_status.latitude
        )
    if command == 'R':
        return ShipStatus(
            bearing=(current_status.bearing+amount) % 360,
            longitude=current_status.longitude,
            latitude=current_status.latitude
        )
    if command == 'F':
        return apply_instruction(current_status, command=BEARING_TO_DIRECTION[current_status.bearing], amount=amount)


def apply_waypoint_instruction(current_ship_status: ShipStatus, current_way_point_location: WayPointLocation,
                               command: str, amount: int) -> Tuple[ShipStatus, WayPointLocation]:
    # print(current_ship_status)
    # print(current_way_point_location)
    # print()
    if command == 'N':
        return (
            current_ship_status,
            WayPointLocation(
                longitude=current_way_point_location.longitude,
                latitude=current_way_point_location.latitude+amount
            )
        )
    if command == 'S':
        return (
            current_ship_status,
            WayPointLocation(
                longitude=current_way_point_location.longitude,
                latitude=current_way_point_location.latitude-amount
            )
        )
    if command == 'E':
        return (
            current_ship_status,
            WayPointLocation(
                longitude=current_way_point_location.longitude+amount,
                latitude=current_way_point_location.latitude
            )
        )
    if command == 'W':
        return (
            current_ship_status,
            WayPointLocation(
                longitude=current_way_point_location.longitude-amount,
                latitude=current_way_point_location.latitude
            )
        )
    if command == 'F':
        return (
            ShipStatus(
                bearing=current_ship_status.bearing,
                longitude=current_ship_status.longitude+amount*current_way_point_location.longitude,
                latitude=current_ship_status.latitude+amount*current_way_point_location.latitude,
            ),
            current_way_point_location
        )
    # clockwise:
    # lon = lat
    # lat = -lon
    if command == 'R':
        if amount > 90:
            return apply_waypoint_instruction(
                current_ship_status,
                WayPointLocation(
                    longitude=current_way_point_location.latitude,
                    latitude=-current_way_point_location.longitude
                ),
                command='R',
                amount=amount-90
            )
        return (
            current_ship_status,
            WayPointLocation(
                longitude=current_way_point_location.latitude,
                latitude=-current_way_point_location.longitude
            )
        )

    # counter clockwise:
    # lon = -lat
    # lat = lon
    if command == 'L':
        if amount > 90:
            return apply_waypoint_instruction(
                current_ship_status,
                WayPointLocation(
                    longitude=-current_way_point_location.latitude,
                    latitude=current_way_point_location.longitude
                ),
                command='L',
                amount=amount-90
            )
        return (
            current_ship_status,
            WayPointLocation(
                longitude=-current_way_point_location.latitude,
                latitude=current_way_point_location.longitude
            )
        )


def main():
    with open('day_12_input.txt') as i:
        instructions = [(line[0], int(line[1:]))for line in i]
    print(instructions)
    initial_status = ShipStatus(bearing=0, longitude=0, latitude=0)
    final_status = reduce(
        lambda current_status, command_tuple: apply_instruction(current_status, *command_tuple),
        instructions, initial_status
    )
    print(final_status)
    print(f'Manhattan distance is {abs(final_status.longitude) + abs(final_status.latitude)}')


def main2():
    with open('day_12_input.txt') as i:
        instructions = [(line[0], int(line[1:]))for line in i]
    print(instructions)
    initial_waypoint_location = WayPointLocation(longitude=10, latitude=1)
    initial_status = ShipStatus(bearing=0, longitude=0, latitude=0)
    final_status = reduce(
        lambda current_status, command_tuple: apply_waypoint_instruction(*current_status, *command_tuple),
        instructions, (initial_status, initial_waypoint_location)
    )
    print(final_status)
    print(f'Manhattan distance is {abs(final_status[0].longitude) + abs(final_status[0].latitude)}')


if __name__ == '__main__':
    main()
    main2()
