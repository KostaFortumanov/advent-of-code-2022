package day4

import readFileLines
import kotlin.math.min

fun main() {
    val sectionPairs = readFileLines("input_4.txt").mapInput()

    println(part1(sectionPairs))
    println(part2(sectionPairs))
}

private fun part1(sectionPairs: List<Pair<Section, Section>>) = sectionPairs.fold(0) { acc, pair ->
    val intersection = pair.intersection()
    val minSectionSize = min(pair.first.size, pair.second.size)
    val fullyContained = intersection.size == minSectionSize

    if (fullyContained) {
        acc + 1
    } else {
        acc
    }
}

private fun part2(sectionPairs: List<Pair<Section, Section>>) = sectionPairs.fold(0) { acc, pair ->
    val intersection = pair.intersection()
    val overlap = intersection.isNotEmpty()

    if (overlap) {
        acc + 1
    } else {
        acc
    }
}

private data class Section(val start: Int, val end: Int) {
    val size: Int
        get() = end - start + 1
}

private fun Pair<Section, Section>.intersection() =
    (this.first.start..this.first.end) intersect (this.second.start..this.second.end)

private fun List<String>.mapInput() = this.map { line ->
    val (elf1, elf2) = line.split(",")
    val (start, end) = elf1.split("-").map { it.toInt() }
    val (start1, end1) = elf2.split("-").map { it.toInt() }
    Section(start, end) to Section(start1, end1)
}
