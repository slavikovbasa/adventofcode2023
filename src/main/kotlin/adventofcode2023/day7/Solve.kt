package adventofcode2023.day7

object Solve {
    const val URL = "https://adventofcode.com/2023/day/7/input"

    fun first(text: String): Long {
        val hands = parse(text).sortedDescending()
        return hands.mapIndexed { idx, hand -> hand.bid * (idx + 1) }.sum()
    }

    fun second(text: String): Long {
        val hands = parseWithJoker(text).sortedDescending()
        return hands.mapIndexed { idx, hand -> hand.bid * (idx + 1) }.sum()
    }
}


fun parse(text: String): List<Hand> {
    val lines = text.trim().lines()
    return lines.map { line ->
        val (cards, bid) = line.split(' ')
        Hand(Cards(cards), bid.toLong())
    }
}

fun parseWithJoker(text: String): List<Hand> {
    val lines = text.trim().lines()
    return lines.map { line ->
        val (cards, bid) = line.split(' ')
        Hand(CardsJokered(cards), bid.toLong())
    }
}

enum class TYPE {
    FIVE_OF_A_KIND {
        override fun match(cards: Map<Char, Int>) = cards.any { it.value == 5 }
    },

    FOUR_OF_A_KIND {
        override fun match(cards: Map<Char, Int>) = cards.any { it.value == 4 }
    },

    FULL_HOUSE {
        override fun match(cards: Map<Char, Int>) = cards.any { it.value == 3 } && cards.any { it.value == 2 }
    },

    THREE_OF_A_KIND {
        override fun match(cards: Map<Char, Int>) = cards.any { it.value == 3 }
    },

    TWO_PAIR {
        override fun match(cards: Map<Char, Int>) = cards.filter { it.value == 2 }.size == 2
    },

    ONE_PAIR {
        override fun match(cards: Map<Char, Int>) = cards.any { it.value == 2 }
    },

    HIGH_CARD {
        override fun match(cards: Map<Char, Int>) = true
    };

    abstract fun match(cards: Map<Char, Int>): Boolean
}

const val RANKS = "AKQJT98765432"

abstract class CardsBase {
    abstract val grouped: Map<Char, Int>
    abstract val ranked: Sequence<Int>
}

class Cards(value: String) : CardsBase() {
    override val grouped = value.groupBy { it }.map { (key, value) -> Pair(key, value.size) }.toMap()
    override val ranked = value.map { RANKS.indexOf(it) }.asSequence()
}

const val RANKS_JOKER = "AKQT98765432J"

class CardsJokered(value: String) : CardsBase() {
    override val grouped = groupByJoker(value)
    override val ranked = value.map { RANKS_JOKER.indexOf(it) }.asSequence()
}

class Hand(private val cards: CardsBase, val bid: Long) : Comparable<Hand> {
    private val typ = TYPE.entries.first { it.match(cards.grouped) }

    override operator fun compareTo(other: Hand): Int {
        if (typ.ordinal != other.typ.ordinal) {
            return typ.ordinal - other.typ.ordinal
        }

        return cards.ranked.zip(other.cards.ranked).map { (first, second) -> first - second }.firstOrNull { it != 0 }
                ?: 0
    }
}

fun groupByJoker(cards: String): Map<Char, Int> {
    val grouped = cards.groupBy { it }.map { (key, value) -> Pair(key, value.size) }.toMap()
    val jokers = grouped['J'] ?: return grouped

    val groupedOptimized = grouped.toMutableMap()
    val maxEntry = groupedOptimized.filter { it.key != 'J' }.maxByOrNull { it.value }
    if (maxEntry == null) {
        return grouped
    }

    groupedOptimized[maxEntry.key] = maxEntry.value + jokers
    groupedOptimized.remove('J')

    return groupedOptimized
}
