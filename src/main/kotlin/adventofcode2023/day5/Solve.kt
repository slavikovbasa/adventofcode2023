package adventofcode2023.day5

object Solve {
    const val URL = "https://adventofcode.com/2023/day/5/input"

    fun first(text: String): Long {
        val lines = text.trim().lineSequence().iterator()
        val seeds = parseSeeds(lines.next())
        lines.next()

        val resMaps = mutableMapOf<String, ResourceMap>();
        while (lines.hasNext()) {
            val resMap = ResourceMap(lines)
            resMaps[resMap.resourceFrom] = resMap
        }

        return seeds.map { matchSeed(it, resMaps) }.min()
    }

    fun second(text: String): Long {
        val lines = text.trim().lineSequence().iterator()
        val seedValues = parseSeeds(lines.next())
        val seeds = seedValues.chunked(2).map { it.first()..<it.first() + it.last() }.flatten()
        lines.next()

        val resMaps = mutableMapOf<String, ResourceMap>();
        while (lines.hasNext()) {
            val resMap = ResourceMap(lines)
            resMaps[resMap.resourceFrom] = resMap
        }

        return seeds.map { matchSeed(it, resMaps) }.min()
    }
}


class RangeMap(val destStart: Long, val sourceStart: Long, val len: Long) {
    fun includes(i: Long) = i in sourceStart..<sourceStart + len
    fun match(i: Long) = if (includes(i)) {
        i - sourceStart + destStart
    } else {
        i
    }
}

fun RangeMap(text: String): RangeMap {
    val parts = text.split(' ').iterator()
    return RangeMap(parts.next().toLong(), parts.next().toLong(), parts.next().toLong())
}

class ResourceMap(val resourceFrom: String, val resourceTo: String, val ranges: Sequence<RangeMap>) {
    fun match(i: Long) = ranges.find { it.includes(i) }?.match(i) ?: i
}

fun ResourceMap(lines: Iterator<String>): ResourceMap {
    val (from, to) = parseResources(lines.next())

    val parts = mutableListOf<RangeMap>()
    while (lines.hasNext()) {
        val line = lines.next().trim()
        if (line.isBlank()) {
            break
        }
        parts.addLast(RangeMap(line))
    }
    return ResourceMap(from, to, parts.asSequence())
}

fun matchSeed(i: Long, resMaps: Map<String, ResourceMap>): Long {
    var resource = "seed";
    var num = i;
    while (true) {
        val resMap = resMaps[resource] ?: break
        resource = resMap.resourceTo
        num = resMap.match(num)
    }
    return num
}

fun parseSeeds(text: String) = text
        .trim()
        .split(": ")
        .elementAt(1)
        .splitToSequence(' ')
        .map(String::toLong)

fun parseResources(text: String): Pair<String, String> {
    val match = Regex("(\\w+)-to-(\\w+) map:").find(text)!!.groupValues
    return Pair(match.elementAt(1), match.elementAt(2))
}
