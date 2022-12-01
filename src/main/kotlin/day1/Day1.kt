package day1

import readFileLines

fun main() {
    val input = readFileLines("input_1.txt").mapInput()

    val runningReduce = input.runningReduce { acc, num ->
        if (num == 0) {
            0
        } else {
            acc + num
        }
    }

    val elfCalories = runningReduce.mapIndexedNotNull { index, num ->
        if (index + 1 < runningReduce.size && runningReduce[index + 1] == 0) {
            num
        } else {
            null
        }
    }

    println(part1(elfCalories))
    println(part2(elfCalories))
}

private fun part1(elfCalories: List<Int>) = elfCalories.max()

private fun part2(elfCalories: List<Int>) = elfCalories.sortedDescending().take(3).sum()

private fun List<String>.mapInput() =
    this.map {
        if (it.isBlank()) {
            0
        } else {
            it.toInt()
        }
    }
