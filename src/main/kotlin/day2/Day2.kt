package day2

import readFileLines

fun main() {
    val lines = readFileLines("input_2.txt")

    val input1 = lines.mapInputPart1()
    println(part1(input1))

    val input2 = lines.mapInputPart2()
    println(part2(input2))
}

private fun part1(input: List<Pair<Shape, Shape>>) = input.fold(0) { acc, it ->
    val roundPoints = when {
        it.second.winAgainst == it.first.toString() -> 6
        it.second.loseAgainst == it.first.toString() -> 0
        else -> 3
    }

    acc + roundPoints + it.second.points
}

private fun part2(input: List<Pair<Shape, String>>) = input.fold(0) { acc, it ->
    val points = when (it.second) {
        "X" -> Shape.valueOf(it.first.winAgainst).points
        "Z" -> Shape.valueOf(it.first.loseAgainst).points + 6
        else -> it.first.points + 3
    }

    acc + points
}

private enum class Shape(val points: Int, val winAgainst: String, val loseAgainst: String) {
    ROCK(1, winAgainst = "SCISSORS", loseAgainst = "PAPER"),
    PAPER(2, winAgainst = "ROCK", loseAgainst = "SCISSORS"),
    SCISSORS(3, winAgainst = "PAPER", loseAgainst = "ROCK");

    companion object {
        fun fromLetter(letter: String) = when (letter) {
            "A", "X" -> ROCK
            "B", "Y" -> PAPER
            else -> SCISSORS
        }
    }
}

private fun List<String>.mapInputPart1() = this.map {
    val parts = it.split(" ")
    Shape.fromLetter(parts[0]) to Shape.fromLetter(parts[1])
}

private fun List<String>.mapInputPart2() = this.map {
    val parts = it.split(" ")
    Shape.fromLetter(parts[0]) to parts[1]
}
