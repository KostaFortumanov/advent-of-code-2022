package day03

import readFileLines

fun main() {
    val input = readFileLines(3)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>) = input.sumOf {
    val mid = it.length / 2
    val firstHalf = it.substring(0 until mid)
    val secondHalf = it.substring(mid)
    val commonItem = firstHalf.first { char -> char in secondHalf }

    itemPriority(commonItem)
}

private fun part2(input: List<String>) = input.chunked(3).sumOf {
    val (elf1, elf2, elf3) = it
    val commonItem = elf1.first { char -> char in elf2.filter { char1 -> char1 in elf3 } }

    itemPriority(commonItem)
}

private fun itemPriority(item: Char): Int {
    val lowerCaseStartPriority = 1
    val upperCaseStartPriority = 27

    return if (item.isLowerCase()) {
        item.code - 'a'.code + lowerCaseStartPriority
    } else {
        item.code - 'A'.code + upperCaseStartPriority
    }
}
