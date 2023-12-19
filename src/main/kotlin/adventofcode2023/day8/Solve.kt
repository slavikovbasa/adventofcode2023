package adventofcode2023.day8

object Solve {
    const val URL = "https://adventofcode.com/2023/day/8/input"

    fun first(text: String): Int {
        val (rules, maps) = parse(text)
        var node = "AAA"
        return generateSequence { rules.asSequence() }.flatten().takeWhile { rule ->
            node = maps[node]!!.byRule(rule)
            node != "ZZZ"
        }.count() + 1
    }

    fun second(text: String): Long {
        val (rules, maps) = parse(text)
        val starts = maps.keys.filter { it.endsWith("A") }
        val indexes = starts.map { getFirstIdx(rules, maps, it)!! }
        return indexes.reduce { acc, it -> lcm(acc, it) }
    }
}

data class Crossroad(val left: String, val right: String)

fun Crossroad.byRule(rule: Char): String = if (rule == 'L') left else right

fun parse(text: String): Pair<String, Map<String, Crossroad>> {
    val lines = text.trim().lines()
    val rules = lines.first()
    val maps = lines.drop(2).associate { line ->
        val (from, to) = line.split(" = ")
        val (left, right) = to.trim('(', ')').split(", ")
        from to Crossroad(left, right)
    }
    return rules to maps
}


fun getFirstIdx(rules: String, maps: Map<String, Crossroad>, start: String): Long? {
    var node = start
    for ((i, rule) in generateSequence { rules.asSequence() }.flatten().withIndex()) {
        node = maps[node]!!.byRule(rule)
        if (node.endsWith('Z')) {
            return i.toLong() + 1
        }
    }
    return null
}

fun lcm(a: Long, b: Long): Long {
    var result = a
    while (result % a != 0L || result % b != 0L) {
        result += a
    }
    return result
}
