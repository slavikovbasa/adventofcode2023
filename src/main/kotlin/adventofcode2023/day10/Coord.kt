package adventofcode2023.day10

data class Coord(val x: Int, val y: Int)

val SOUTH = Coord(0, +1)
val EAST = Coord(+1, 0)
val NORTH = Coord(0, -1)
val WEST = Coord(-1, 0)

operator fun Coord.plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)

operator fun Coord.minus(other: Coord) = Coord(this.x - other.x, this.y - other.y)

operator fun Coord.unaryMinus() = Coord(-this.x, -this.y)

operator fun Coord.compareTo(other: Coord) =
    if (this.x < other.x && this.y < other.y) {
        -1
    } else if (this.x > other.x && this.y > other.y) {
        1
    } else {
        0
    }

fun Coord.left() = when (this) {
    SOUTH -> WEST
    EAST -> SOUTH
    NORTH -> EAST
    WEST -> NORTH
    else -> null
}

fun Coord.right() = this.left()?.let { -it }

fun Coord.neighbors() = sequence { yieldAll(listOf(SOUTH, EAST, NORTH, WEST).map { this@neighbors + it }) }

fun splitNeighbors(line: Pair<Coord, Coord>?): Pair<List<Coord>?, List<Coord>?> {
    val (from, to) = line ?: return null to null

    val leftCoords = setOfNotNull(from.left(), (-to).left()).filter { it != from && it != to }.ifEmpty { null }
    val rightCoords = setOfNotNull(from.right(), (-to).right()).filter { it != from && it != to }.ifEmpty { null }

    return leftCoords to rightCoords
}