package adventofcode2023.day4

object Solve {
    const val URL = "https://adventofcode.com/2023/day/4/input"

    fun first(text: String): Int {
        return text.trim().lines().sumOf { Card(it).points }
    }

    fun second(text: String): Int {
        val cardsPoints = text.trim().lines().map { Card(it).numMatching }
        val cardsNum = Array(cardsPoints.size) { 1 }
        for ((index, value) in cardsPoints.withIndex()) {
            for (i in 1..value) {
                cardsNum[index + i] += cardsNum[index]
            }
        }
        return cardsNum.sum()
    }
}

class Card(val numMatching: Int) {
    val points = if (numMatching == 0) {
        0
    } else {
        1 shl (numMatching - 1)
    }
}

fun Card(text: String): Card {
    val numsAll = text.trim().split(": ").last()
    val numParts = numsAll.split(" | ")
    val winningNums = numStringToSet(numParts.first())
    val nums = numStringToSet(numParts.elementAt(1))
    return Card(numMatching = nums.intersect(winningNums).size)
}

fun numStringToSet(str: String) = str.trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
