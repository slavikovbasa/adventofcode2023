package adventofcode2023.day5

object Solve {
    const val URL = "https://adventofcode.com/2023/day/5/input"

    fun first(text: String): Long {
        val (seeds, resMaps) = parse(text);
        return seeds.minOf { matchSeed(it, resMaps) }
    }

    fun second(text: String): Long {
        val (seedValues, resMaps) = parse(text);
        val seeds = seedValues.chunked(2).map { it.first()..<it.first() + it.last() }

        return minSeedLocation(seeds, resMaps)
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

    val resMaps = mutableMapOf<String, ResourceMap>();
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
        val dstStart = (srcRanges.second.first - src.first) + dst.first
        val dstRange =
                dstStart..(srcRanges.second.last - srcRanges.second.first) + dstStart
        return Triple(srcRanges.first, dstRange, srcRanges.third)
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
        var last = range;
        for (r in rangeMaps) {
            if (last.isEmpty()) {
                break
            }
            val (first, second, third) = r.match(last)
            if (!first.isEmpty()) {
                result.add(first)
            }
            if (!second.isEmpty()) {
                result.add(second)
            }
            last = third
        }
        if (!last.isEmpty()) {
            result.add(last)
        }
        return result
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

fun minSeedLocation(seedRanges: List<LongRange>, resMaps: Map<String, ResourceMap>): Long {
    var resource = "seed";
    var ranges = seedRanges;
    while (true) {
        val resMap = resMaps[resource] ?: break
        resource = resMap.resourceTo
        ranges = ranges.flatMap { resMap.match(it) }.sortedBy { it.first }
    }
    return ranges.first().first
}

fun matchSeed(i: Long, resMaps: Map<String, ResourceMap>): Long {
    var resource = "seed";
    var num = i;
    while (true) {
        val resMap = resMaps[resource] ?: break
        resource = resMap.resourceTo
        num = resMap.matchNum(num)
    }
    return num
}

fun splitRange(range: LongRange, by: LongRange): Triple<LongRange, LongRange, LongRange> {
    val first = range.first..minOf(range.last, by.first - 1)
    val second = maxOf(range.first, by.first)..minOf(range.last, by.last)
    val third = maxOf(range.first, by.last + 1)..range.last
    return Triple(first, second, third)
}
