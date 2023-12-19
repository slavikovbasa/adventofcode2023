package adventofcode2023.day10

enum class TileType(val symbol: Char, val dirs: Array<Coord>) {
    VERTICAL('|', arrayOf(NORTH, SOUTH)),
    HORIZONTAL('-', arrayOf(WEST, EAST)),
    NE_BEND('L', arrayOf(NORTH, EAST)),
    NW_BEND('J', arrayOf(NORTH, WEST)),
    SW_BEND('7', arrayOf(SOUTH, WEST)),
    SE_BEND('F', arrayOf(SOUTH, EAST)),
    GROUND('.', arrayOf()),
    CROSS('S', arrayOf(SOUTH, EAST, NORTH, WEST)),
}

fun TileType.oriented(dir: Coord): Pair<Coord, Coord>? {
    if (this.dirs.size != 2 || dir !in this.dirs) {
        return null
    }

    val (from, to) = this.dirs.partition { it == dir }.let {
        it.first.first() to it.second.first()
    }

    return from to to
}

data class Tile(val typ: TileType, val coord: Coord)
