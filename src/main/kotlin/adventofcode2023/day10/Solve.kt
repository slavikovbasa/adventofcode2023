package adventofcode2023.day10


object Solve {
    const val URL = "https://adventofcode.com/2023/day/10/input"

    fun first(text: String): Int {
        val (start, map) = parse(text)
        val pipe = getPipe(map[start]!!, map)
        return (pipe.size + 1) / 2
    }

    fun second(text: String): Int {
        val (start, map) = parse(text)
        val pipe = getPipe(map[start]!!, map)

        val maxCoord = Coord(map.keys.maxOf { it.x }, map.keys.maxOf { it.y })

        val (left, right) = getNeighbors(map[start]!!, pipe, map)
        val innerCoords = maybeExpandSet(left, pipe, maxCoord) ?: maybeExpandSet(right, pipe, maxCoord)!!
        return innerCoords.size
    }
}

fun parse(text: String): Pair<Coord, Map<Coord, Tile>> {
    val map = mutableMapOf<Coord, Tile>()
    var start: Coord? = null

    for ((i, line) in text.trim().lines().withIndex()) {
        for ((j, ch) in line.trim().withIndex()) {
            val coord = Coord(j, i)
            val typ = TileType.entries.first { it.symbol == ch }
            if (typ == TileType.CROSS) {
                start = coord
            }
            map[coord] = Tile(typ, coord)
        }
    }

    return start!! to map
}

fun getPipe(start: Tile, map: Map<Coord, Tile>): Map<Coord, Coord> {

    fun nextFor(t: Tile, predicate: (Tile) -> Boolean) =
        t.typ.dirs.mapNotNull { map[it + t.coord] }.firstOrNull { predicate(it) }

    var tile = nextFor(start) { nextFor(it) { t -> t == start } != null }!!

    var prevTile = start
    val pipe = mutableMapOf(prevTile.coord to tile.coord)

    while (tile != start) {
        val nextTile = nextFor(tile) { it != prevTile }!!
        pipe[tile.coord] = nextTile.coord
        prevTile = tile.also { tile = nextTile }
    }

    return pipe
}

fun getNeighbors(
    start: Tile,
    pipe: Map<Coord, Coord>,
    map: Map<Coord, Tile>,
): Pair<MutableSet<Coord>, MutableSet<Coord>> {
    val leftSet = mutableSetOf<Coord>()
    val rightSet = mutableSetOf<Coord>()

    for ((prev, curr) in iterPipe(pipe, start.coord)) {
        val tile = map[curr]!!
        val (left, right) = splitNeighbors(tile.typ.oriented(prev - curr))

        left?.map { it + tile.coord }?.filter { it !in pipe }?.let(leftSet::addAll)
        right?.map { it + tile.coord }?.filter { it !in pipe }?.let(rightSet::addAll)
    }

    return leftSet to rightSet
}

fun iterPipe(pipe: Map<Coord, Coord>, start: Coord) = sequence {
    var prev = start
    var curr = pipe[prev]!!
    while (curr != start) {
        yield(prev to curr)
        prev = curr.also { curr = pipe[curr]!! }
    }
}

fun maybeExpandSet(
    set: MutableSet<Coord>,
    pipe: Map<Coord, Coord>,
    maxCoord: Coord,
): Set<Coord>? {
    val toProcess = set.toMutableList()

    while (toProcess.isNotEmpty()) {
        val next = toProcess.removeFirst()
        if (next < Coord(0, 0) || next > maxCoord) {
            set.clear()
            return null
        }
        val neighbors = next.neighbors().filter { it !in set && it !in pipe }.toList()
        if (neighbors.isNotEmpty()) {
            toProcess.addAll(neighbors)
            set.addAll(neighbors)
        }
    }

    return set
}
