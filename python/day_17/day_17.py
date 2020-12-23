
# Ideas
# Keep track of the points that are active.
# For each iteration:
# 1. Find neighbouring points for each active point
# 2. Check if the points found change state + the currently active nodes.

# If a cube is active and exactly 2 or 3 of its neighbors are also active,
# the cube remains active. Otherwise, the cube becomes inactive.

# If a cube is inactive but exactly 3 of its neighbors are activ§e,
# the cube becomes active. Otherwise, the cube remains inactive.

from typing import List, NamedTuple, Set


class Point(NamedTuple):
    x: int
    y: int
    z: int
    w: int


def get_points(filename: str) -> Set[Point]:
    points = []
    with open(filename) as i:
        for row, line in enumerate(i):
            for col, value in enumerate(line.strip()):
                if value == '#':
                    points.append(Point(x=col, y=row, z=0, w=0))
    return set(points)


def print_points(points: Set[Point], z: int):
    print(f'Print points on z={z}')
    for p in sorted(points):
        if p.z == z:
            print(p)


class PocketSpace:

    def __init__(self, filename: str, iterations: int):
        self.active_points = get_points(filename)
        print(f'There are {len(self.active_points)} active points')
        print(self.active_points)
        self.iterations = iterations

    @property
    def num_active_points(self) -> int:
        return len(self.active_points)

    def run_iterations(self):
        for i in range(self.iterations):
            self.run_iteration()

    def run_iteration(self):
        all_neighbours = self.get_all_inactive_neighbours()
        new_active_points = set()
        print(f'Number of active points is: {len(self.active_points)}')
        print(f'Number of inactive points is: {len(all_neighbours)}')
        for point in all_neighbours:
            assert point not in self.active_points
            # print(f'Processing point {point}')
            active_count = self.get_active_neighbour_count(point)
            # print(f'It has {active_count} active neighbours')
            if active_count == 3:
                new_active_points.add(point)
        for point in self.active_points:
            assert point not in all_neighbours
            # print(f'Processing active point {point}')
            active_count = self.get_active_neighbour_count(point)
            # print(f'It has {active_count} active neighbours')
            if 2 <= active_count <= 3:
                new_active_points.add(point)
        print(f'Number of active points after one iteration is {len(new_active_points)}')
        self.active_points = new_active_points

    def get_active_neighbour_count(self, point: Point) -> int:
        active_count = 0
        for x in [point.x-1, point.x, point.x+1]:
            for y in [point.y-1, point.y, point.y+1]:
                for z in [point.z-1, point.z, point.z+1]:
                    for w in [point.w-1, point.w, point.w+1]:
                        n_point = Point(x=x, y=y, z=z, w=w)
                        if n_point in self.active_points:
                            active_count += 1
        if point in self.active_points:
            active_count -= 1
        return active_count

    def get_all_inactive_neighbours(self) -> List[Point]:
        all_neighbours = set()
        for point in self.active_points:
            all_neighbours.update(set(self.get_neighbours_for_point(point)))
        return list(all_neighbours - self.active_points)

    def get_neighbours_for_point(self, point: Point) -> List[Point]:
        neighbours = []
        for x in [point.x-1, point.x, point.x+1]:
            for y in [point.y-1, point.y, point.y+1]:
                for z in [point.z-1, point.z, point.z+1]:
                    for w in [point.w-1, point.w, point.w+1]:
                        n_point = Point(x=x, y=y, z=z, w=w)
                        if point != n_point:
                            neighbours.append(n_point)
        return neighbours


if __name__ == '__main__':
    ps = PocketSpace('day_17_input.txt', iterations=6)
    ps.run_iterations()
    print(ps.num_active_points)
