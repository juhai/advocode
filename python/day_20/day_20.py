import enum
from collections import Counter
from typing import NamedTuple, List, Dict, Set

import math


class Edge(NamedTuple):
    edge: str
    unique: bool


class Tile(NamedTuple):
    id: int
    distinct_edges: int  # Number of edges not share with any other tile
    edges: Dict[str, Edge]  # Keys: top, right, bottom, left


def read_tiles(filename: str) -> List[Tile]:
    tile_size = 10
    tiles = []
    with open(filename) as i:
        while True:
            tile = i.readline()
            if not tile:
                break
            tile_id = int(tile.strip().strip(':').split()[1])
            tile_data = []
            for _ in range(tile_size):
                tile_data.append(i.readline().strip())
            edges = {
                'top': Edge(edge=tile_data[0], unique=False),
                'right': Edge(edge=''.join(t[-1] for t in tile_data), unique=False),
                'bottom': Edge(edge=tile_data[-1], unique=False),
                'left': Edge(edge=''.join([t[0] for t in tile_data]), unique=False),
            }
            tiles.append(Tile(
                id=tile_id,
                distinct_edges=-1,
                edges=edges
            ))
            i.readline()
    return tiles


def get_distinct_edges(tiles: List[Tile]) -> List[Tile]:
    edge_tile_pairs = {(tile.id, edge.edge) for tile in tiles for edge in tile.edges.values()}
    edge_counts = Counter(edge for _, edge in edge_tile_pairs)
    new_tiles = []
    for tile in tiles:
        new_edges = {}
        for key, edge in tile.edges.items():
            new_edges[key] = Edge(edge.edge, unique=edge_counts[edge.edge] == 1)

        unique_edge_counts = len(list(filter(lambda edge: edge.unique, new_edges.values())))
        new_tiles.append(Tile(
            id=tile.id,
            distinct_edges=unique_edge_counts,
            edges=new_edges
        ))
    print(Counter(tile.distinct_edges for tile in new_tiles))
    return new_tiles


def flip_tiles(tiles: List[Tile]) -> List[Tile]:
    flipped_tiles = []
    for tile in tiles:
        new_edges = {
            'top': Edge(edge=tile.edges['top'].edge[::-1], unique=False),
            'right': Edge(edge=tile.edges['left'].edge, unique=False),
            'bottom': Edge(edge=tile.edges['bottom'].edge[::-1], unique=False),
            'left': Edge(edge=tile.edges['right'].edge, unique=False),
        }
        flipped_tiles.append(Tile(
            id=tile.id,
            distinct_edges=tile.distinct_edges,
            edges=new_edges
        ))
    return tiles + flipped_tiles


def rotate_tiles(tiles: List[Tile]) -> List[Tile]:
    rotated_tiles = []
    for tile in tiles:
        prev = tile.edges
        for i in range(1, 4):
            edges = {
                'top': Edge(edge=prev['left'].edge[::-1], unique=False),
                'right': Edge(edge=prev['top'].edge, unique=False),
                'bottom': Edge(edge=prev['right'].edge[::-1], unique=False),
                'left': Edge(edge=prev['bottom'].edge, unique=False)
            }
            rotated_tiles.append(Tile(
                id=tile.id,
                distinct_edges=tile.distinct_edges,
                edges=edges
            ))
            prev = edges
    return tiles + rotated_tiles


class QueueItem(NamedTuple):
    tiles: List[Tile]
    next_tile: int
    already_matched_tiles: Set[int]


class PieceType(enum.Enum):
    TOP_LEFT = 0
    TOP_RIGHT = 1
    BOTTOM_LEFT = 2
    BOTTOM_RIGHT = 3
    TOP = 4
    RIGHT = 5
    BOTTOM = 6
    LEFT = 7
    INNER = 8


def get_piece_type(index: int, grid_size: int) -> PieceType:
    if index == 0:
        return PieceType.TOP_LEFT
    if index == grid_size - 1:
        return PieceType.TOP_RIGHT
    if index == (grid_size-1) * grid_size:
        return PieceType.BOTTOM_LEFT
    if index == grid_size * grid_size - 1:
        return PieceType.BOTTOM_RIGHT
    if index % grid_size == 0:
        return PieceType.LEFT
    if index % (grid_size-1) == 0:
        return PieceType.RIGHT
    if 1 <= index < grid_size-1:
        return PieceType.TOP
    if (grid_size-1) * grid_size < index < grid_size * grid_size - 1:
        return PieceType.BOTTOM
    return PieceType.INNER


def get_matching_tiles(tiles: List[Tile], previous_tiles: List[Tile], prev_edges: List[str],
                       this_edges: List[str]) -> List[Tile]:
    tiles_p_t_edges = zip(previous_tiles, prev_edges, this_edges)
    return [
        tile for tile in tiles
        if all(prev_tile.edges[p].edge == tile.edges[t].edge for prev_tile, p, t in tiles_p_t_edges)
    ]


def fit_tiles(all_tiles: Dict[PieceType, List[Tile]], grid_size: int) -> List[Tile]:
    grid = [None] * grid_size
    for i in range(len(grid)):
        grid[i] = [None] * grid_size

    # Start fitting the puzzle from top left corner and add one piece at a time from top to bottom,
    # left to right
    queue = [
        QueueItem(tiles=[tile], next_tile=1, already_matched_tiles={tile.id}) for tile in all_tiles[PieceType.TOP_LEFT]
    ]
    while queue:
        next_item = queue.pop(0)
        if next_item.next_tile == grid_size * grid_size:
            return next_item.tiles
        piece_type = get_piece_type(next_item.next_tile, grid_size)

        possible_tiles = []
        if piece_type in [PieceType.TOP, PieceType.TOP_RIGHT]:
            possible_tiles = get_matching_tiles(
                all_tiles[piece_type], [next_item.tiles[-1]], ['right'], ['left'])
        elif piece_type in [PieceType.LEFT, PieceType.BOTTOM_LEFT]:
            possible_tiles = get_matching_tiles(
                all_tiles[piece_type], [next_item.tiles[-grid_size]],
                ['bottom'], ['top'])
        elif piece_type in [PieceType.RIGHT, PieceType.BOTTOM, PieceType.INNER, PieceType.BOTTOM_RIGHT]:
            possible_tiles = get_matching_tiles(
                all_tiles[piece_type], [next_item.tiles[-grid_size], next_item.tiles[-1]],
                ['bottom', 'right'], ['top', 'left'])

        for possible_tile in possible_tiles:
            if possible_tile.id in next_item.already_matched_tiles:
                continue
            queue.insert(0, QueueItem(
                tiles=next_item.tiles + [possible_tile], next_tile=next_item.next_tile+1,
                already_matched_tiles=next_item.already_matched_tiles | {possible_tile.id}))


def main():
    tiles = read_tiles('day_20_input.txt')
    grid_size = int(math.sqrt(len(tiles)) + 0.5)
    # print(len(tiles), tiles)
    tiles = flip_tiles(tiles)
    # print(len(tiles), tiles)

    tiles = rotate_tiles(tiles)
    # print(len(tiles), tiles)
    tiles = get_distinct_edges(tiles)
    # print(tiles)
    corner_tiles = [tile for tile in tiles if tile.distinct_edges == 2]
    edge_tiles = [tile for tile in tiles if tile.distinct_edges == 1]
    inner_tiles = [tile for tile in tiles if tile.distinct_edges == 0]

    top_left_corner_tiles = [
        tile for tile in corner_tiles if tile.edges['top'].unique and tile.edges['left'].unique]
    top_right_corner_tiles = [
        tile for tile in corner_tiles if tile.edges['top'].unique and tile.edges['right'].unique]
    bottom_left_corner_tiles = [
        tile for tile in corner_tiles if tile.edges['bottom'].unique and tile.edges['left'].unique]
    bottom_right_corner_tiles = [
        tile for tile in corner_tiles if tile.edges['bottom'].unique and tile.edges['right'].unique]

    top_tiles = [tile for tile in edge_tiles if tile.edges['top'].unique]
    right_tiles = [tile for tile in edge_tiles if tile.edges['right'].unique]
    bottom_tiles = [tile for tile in edge_tiles if tile.edges['bottom'].unique]
    left_tiles = [tile for tile in edge_tiles if tile.edges['left'].unique]

    all_tiles = {
        PieceType.INNER: inner_tiles,
        PieceType.TOP_LEFT: top_left_corner_tiles,
        PieceType.TOP_RIGHT: top_right_corner_tiles,
        PieceType.BOTTOM_LEFT: bottom_left_corner_tiles,
        PieceType.BOTTOM_RIGHT: bottom_right_corner_tiles,
        PieceType.TOP: top_tiles,
        PieceType.RIGHT: right_tiles,
        PieceType.BOTTOM: bottom_tiles,
        PieceType.LEFT: left_tiles
    }

    print(f'There are {len(inner_tiles)} inner tiles')

    print(f'There are {len(top_left_corner_tiles)} top left corner tiles')
    print(f'There are {len(top_right_corner_tiles)} top right corner tiles')
    print(f'There are {len(bottom_left_corner_tiles)} bottom left corner tiles')
    print(f'There are {len(bottom_right_corner_tiles)} bottom right corner tiles')

    print(f'There are {len(top_tiles)} top edge tiles')
    print(f'There are {len(right_tiles)} right edge tiles')
    print(f'There are {len(bottom_tiles)} bottom edge tiles')
    print(f'There are {len(left_tiles)} left edge tiles')

    result = fit_tiles(all_tiles, grid_size)
    # print(result)
    print([tile.id for tile in result])
    print('Result is', result[0].id * result[grid_size-1].id * result[-1].id * result[-grid_size].id)


if __name__ == '__main__':
    main()
