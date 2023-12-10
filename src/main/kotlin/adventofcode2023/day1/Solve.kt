package adventofcode2023.day1

object Solve {
    const val URL = "https://adventofcode.com/2023/day/1/input"
    
    fun first(text: String): Int {
        return text.trim().lines().sumOf { digitsFromLineSimple(it) }
    }
    
    fun second(text: String): Int {
        return text.trim().lines().sumOf { digitsFromLineComplex(it) }
    }
}

fun digitsFromLineSimple(line: String): Int {
    val digits = line.map { c -> c.digitToIntOrNull() }.filterNotNull()
    return "${digits.first()}${digits.last()}".toInt()
}


val numbers = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
    "zero" to 0,
)

fun digitsFromLineComplex(line: String): Int {
    var first: Int? = null;
    var last: Int? = null;
    for (i in line.indices) {
        if (line[i].isDigit()) {
            last = line[i].digitToInt()
        } else {
            val slice = line.substring(i)
            val strNum = numbers.firstNotNullOfOrNull {
                if (slice.startsWith(it.key)) { it.value } else { null }
            }
            if (strNum != null) {
                last = strNum
            }
        }
        if (first == null && last != null) {
            first = last
        }
    }

    return "${first}${last}".toInt()
}
