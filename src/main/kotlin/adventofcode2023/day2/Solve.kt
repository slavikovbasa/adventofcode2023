package adventofcode2023.day2

object Solve {
    const val URL = "https://adventofcode.com/2023/day/2/input"

    fun first(text: String): Int {
        val totalCubes = mapOf(
                Color.RED to 12,
                Color.GREEN to 13,
                Color.BLUE to 14,
        )

        return text.trim().lines().map { Game(it) }.filter { it.isValidFor(totalCubes) }.sumOf { it.id }
    }

    fun second(text: String): Int {
        return text.trim().lines().sumOf {
            Game(it).getMinCubeset().values.reduce { acc, i -> acc * i }
        }
    }
}

enum class Color { RED, GREEN, BLUE }

class Game(val id: Int, private val cubesets: List<Map<Color, Int>>) {
    fun isValidFor(totalCubes: Map<Color, Int>): Boolean {
        return cubesets.all { it.all { (key, value) -> value <= totalCubes[key]!! } }
    }

    fun getMinCubeset(): Map<Color, Int> {
        val minCubeset = mutableMapOf(Color.RED to 0, Color.GREEN to 0, Color.BLUE to 0)

        for (cubeset in cubesets) {
            for ((key, value) in cubeset) {
                if (minCubeset[key]!! < value) {
                    minCubeset[key] = value
                }
            }
        }

        return minCubeset
    }
}

fun Game(str: String): Game {
    val gameParts = str.split(": ")

    val id = gameParts[0].split(" ")[1].toInt()
    val cubesets = gameParts[1].split("; ").map { line ->
        line.split(", ").associate { parseCubes(it) }
    }

    return Game(id, cubesets)
}

fun parseCubes(str: String): Pair<Color, Int> {
    val parts = str.split(' ')
    return Pair(Color.valueOf(parts[1].uppercase()), parts[0].toInt())
}
