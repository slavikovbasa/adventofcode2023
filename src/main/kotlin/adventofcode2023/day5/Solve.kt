package adventofcode2023.day5

object Solve {
    const val URL = "https://adventofcode.com/2023/day/5/input"

    fun first(text: String): Long {
        val (seeds, resMaps) = parse(text)
        var resource = "seed"
        var nums = seeds
        while (true) {
            val resMap = resMaps[resource] ?: break
            resource = resMap.resourceTo
            nums = nums.map { resMap.matchNum(it) }.sorted()
        }
        return nums.first()
    }

    fun second(text: String): Long {
        val (seedValues, resMaps) = parse(text)
        val seeds = seedValues.chunked(2).map { it.first()..<it.first() + it.last() }

        var resource = "seed"
        var ranges = seeds
        while (true) {
            val resMap = resMaps[resource] ?: break
            resource = resMap.resourceTo
            ranges = ranges.flatMap { resMap.match(it) }.sortedBy { it.first }
        }
        return ranges.first().first
    }
}

fun parse(text: String): Pair<List<Long>, Map<String, ResourceMap>> {
    val lines = text.lines().iterator()
    val seeds = lines.next()
            .trim()
            .split(": ")
            .elementAt(1)
            .split(' ')
            .map(String::toLong)

    lines.next()

    val resMaps = mutableMapOf<String, ResourceMap>()
    while (lines.hasNext()) {
        val resMap = ResourceMap(lines)
        resMaps[resMap.resourceFrom] = resMap
    }
    return Pair(seeds, resMaps)
}

class RangeMap(val src: LongRange, private val dst: LongRange) {
    fun matchNum(i: Long) = if (src.contains(i)) {
        i - src.first + dst.first
    } else {
        i
    }

    fun match(range: LongRange): Triple<LongRange, LongRange, LongRange> {
        val srcRanges = splitRange(range, src)
        val dstStart = srcRanges.second.first - src.first + dst.first
        val dstEnd = srcRanges.second.last - src.first + dst.first

        return Triple(srcRanges.first, dstStart..dstEnd, srcRanges.third)
    }
}

fun RangeMap(text: String): RangeMap {
    val (dstStart, srcStart, len) = text.split(' ').map(String::toLong)
    return RangeMap(srcStart..<srcStart + len, dstStart..<dstStart + len)
}

class ResourceMap(val resourceFrom: String, val resourceTo: String, private val rangeMaps: List<RangeMap>) {

    fun matchNum(i: Long) = rangeMaps.find { it.src.contains(i) }?.matchNum(i) ?: i

    fun match(range: LongRange): List<LongRange> {
        val result = mutableListOf<LongRange>()
        var nextRange = range
        for (map in rangeMaps) {
            if (nextRange.isEmpty()) {
                break
            }
            val (first, second, third) = map.match(nextRange)
            result.add(first)
            result.add(second)
            nextRange = third
        }
        result.add(nextRange)
        return result.filter { !it.isEmpty() }
    }
}

fun ResourceMap(lines: Iterator<String>): ResourceMap {
    val (_, from, to) = Regex("(\\w+)-to-(\\w+) map:").find(lines.next())!!.groupValues

    val parts = mutableListOf<RangeMap>()
    while (true) {
        val line = lines.next().trim()
        if (line.isBlank()) {
            break
        }
        parts.add(RangeMap(line))
    }
    return ResourceMap(from, to, parts.sortedBy { it.src.first })
}

fun splitRange(range: LongRange, by: LongRange): Triple<LongRange, LongRange, LongRange> {
    val first = range.first..minOf(range.last, by.first - 1)
    val second = maxOf(range.first, by.first)..minOf(range.last, by.last)
    val third = maxOf(range.first, by.last + 1)..range.last
    return Triple(first, second, third)
}
