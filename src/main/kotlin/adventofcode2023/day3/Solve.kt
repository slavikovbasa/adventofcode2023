package adventofcode2023.day3

object Solve {
    const val URL = "https://adventofcode.com/2023/day/3/input"

    fun first(text: String): Int {
        val schema = EngineSchema(text)
        return schema.partNumbers().sumOf { it.symbols.toInt() }
    }

    fun second(text: String): Int {
        val schema = EngineSchema(text)
        return schema.gearRatios().reduce { acc, i -> acc + i }
    }
}

typealias Coord = Pair<Int, Int>

enum class ElementType(val isSymbol: Boolean) {
    NUMBER(false),
    STAR(true),
    EMPTY(false),
    OTHER(true),
}

class Element(val symbols: String, val coords: Coord, val typ: ElementType) {
    fun adjacentCoords() = sequence {
        val xEnd = coords.second + symbols.length

        for (i in coords.first - 1..coords.first + 1) {
            for (j in coords.second - 1..xEnd) {
                if (i == coords.first && j < xEnd && j >= coords.second) {
                    continue
                }
                yield(Coord(i, j))
            }
        }
    }
}

class EngineSchema(private val elements: Map<Coord, Element>) {
    fun partNumbers(): List<Element> = elements.values.toSet().filter { it.typ == ElementType.NUMBER }.filter {
        it.adjacentCoords().any { coord -> elements[coord]?.typ?.isSymbol == true }
    }

    fun gearRatios() = elements.values.filter { it.typ == ElementType.STAR }.map {
        it.adjacentCoords().map { coord -> elements[coord] }
                .filterNotNull()
                .toSet()
                .filter { el -> el.typ == ElementType.NUMBER }
    }.filter { it.size == 2 }.map { it.map { el -> el.symbols.toInt() }.reduce { acc, i -> acc * i } }
}

fun EngineSchema(str: String): EngineSchema {
    val elements = mutableMapOf<Coord, Element>()
    for ((i, row) in str.trim().lines().withIndex()) {
        var j = 0
        while (j < row.length) {
            val element = when {
                row[j].isDigit() -> Element(row.drop(j).takeWhile(Char::isDigit), Coord(i, j), ElementType.NUMBER)
                row[j] == '*' -> Element("*", Coord(i, j), ElementType.STAR)
                row[j] == '.' -> Element(".", Coord(i, j), ElementType.EMPTY)
                else -> Element(row[j].toString(), Coord(i, j), ElementType.OTHER)
            }
            j += element.symbols.length
            if (element.typ != ElementType.EMPTY) {
                for (newJ in element.coords.second..<j) {
                    elements[Coord(i, newJ)] = element
                }
            }
        }
    }
    return EngineSchema(elements.toMap())
}
