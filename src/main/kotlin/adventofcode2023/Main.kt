package adventofcode2023

import adventofcode2023.day3.Solve
import kotlin.time.DurationUnit
import kotlin.time.measureTime

suspend fun main() {
    val text = fetch(Solve.URL)

    val elapsed1 = measureTime {
        val res = Solve.first(text)
        println("res1: $res")
    }
    println("elapsed: ${elapsed1.toString(DurationUnit.SECONDS, 6)}")

    val elapsed2 = measureTime {
        val res = Solve.second(text)
        println("res2: $res")
    }
    println("elapsed: ${elapsed2.toString(DurationUnit.SECONDS, 6)}")
}
