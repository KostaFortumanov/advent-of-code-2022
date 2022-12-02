package day2

import readFileLines

const val WIN_POINTS: Int = 6
const val LOSE_POINTS: Int = 6
const val DRAW_POINTS: Int = 6

fun main() {
    val lines = readFileLines("input_2.txt")

    val input1 = lines.mapInputPart1()
    println(part1(input1))

    val input2 = lines.mapInputPart2()
    println(part2(input2))
}

private fun part1(input: List<Pair<Shape, Shape>>) = input.fold(0) { acc, it ->
    val roundPoints = when {
        it.second.winAgainst == it.first.toString() -> WIN_POINTS
        it.second.loseAgainst == it.first.toString() -> LOSE_POINTS
        else -> DRAW_POINTS
    }

    acc + roundPoints + it.second.points
}

private fun part2(input: List<Pair<Shape, String>>) = input.fold(0) { acc, it ->
    val points = when (it.second) {
        "X" -> Shape.valueOf(it.first.winAgainst).points + LOSE_POINTS
        "Z" -> Shape.valueOf(it.first.loseAgainst).points + WIN_POINTS
        else -> it.first.points + DRAW_POINTS
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
