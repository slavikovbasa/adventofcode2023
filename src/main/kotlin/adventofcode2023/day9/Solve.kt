package adventofcode2023.day9


object Solve {
    const val URL = "https://adventofcode.com/2023/day/9/input"

    fun first(text: String): Int {
        val histories = parse(text)
        return histories.sumOf { nextVal(it, 0) }
    }

    fun second(text: String): Int {
        val histories = parse(text)
        return histories.map(::prevVal).sum()
    }
}

fun parse(text: String): List<List<Int>> = text.trim().lines().map { it.trim().split(' ').map(String::toInt) }

tailrec fun nextVal(history: List<Int>, acc: Int): Int {
    val derivative = history.windowed(size = 2) { it.last() - it.first() }
    if (derivative.all { it == 0 }) {
        return acc + history.last()
    }
    return nextVal(derivative, acc + history.last())
}

fun prevVal(history: List<Int>): Int {
    val derivative = history.windowed(size = 2) { it.last() - it.first() }
    if (derivative.all { it == 0 }) {
        return history.first()
    }
    return history.first() - prevVal(derivative)
}