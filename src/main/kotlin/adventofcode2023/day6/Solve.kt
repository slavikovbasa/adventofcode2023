package adventofcode2023.day6

import java.math.BigInteger
import kotlin.math.*

object Solve {
    const val URL = "https://adventofcode.com/2023/day/6/input"

    fun first(text: String): Long {
        val races = parse(text)
        return races.map { it.solve() }.reduce { acc, i -> acc * i }
    }

    fun second(text: String): Long {
        val race = parse(text).reduce { acc, r ->
            Race("${acc.totalTime}${r.totalTime}".toLong(), "${acc.record}${r.record}".toLong())
        }
        return race.solve()
    }
}


fun parse(text: String): List<Race> {
    val lines = text.trim().lines()
    val times = lines.first().split(":").elementAt(1).trim().split("\\s+".toRegex()).map(String::toLong)
    val distances = lines.last().split(":").elementAt(1).trim().split("\\s+".toRegex()).map(String::toLong)
    return times.zip(distances).map { Race(it.first, it.second) }
}

class Race(val totalTime: Long, val record: Long) {
    fun solve(): Long {
        val d = sqrt((totalTime * totalTime - 4 * record).toDouble())
        val t1 = (totalTime - d) / 2
        val t2 = (totalTime + d) / 2
        val a1 = if (ceil(t1) != t1) {
            ceil(t1)
        } else {
            t1 + 1
        }
        val a2 = if (floor(t2) != t2) {
            floor(t2)
        } else {
            t2 - 1
        }
        return (a2 - a1).toLong() + 1
    }
}
