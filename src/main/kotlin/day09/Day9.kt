package day09

import inRange
import readFileLines
import kotlin.math.abs

fun main() {
    val input = readFileLines(9)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int = tailNumPositions(input, 2)

private fun part2(input: List<String>): Int = tailNumPositions(input, 10)

private fun tailNumPositions(input: List<String>, ropeLength: Int): Int {
    val tails = MutableList(ropeLength) { Position(0, 0) }
    val tailPositions = mutableSetOf(tails.last())

    input.forEach { line ->
        val (direction, numMoves) = line.split(" ")
        repeat(numMoves.toInt()) {
            tails[0] += when (direction) {
                "U" -> Position(0, 1)
                "D" -> Position(0, -1)
                "L" -> Position(-1, 0)
                else -> Position(1, 0)
            }
            for (tailIndex in 0 until ropeLength - 1) {
                val distanceX = tails[tailIndex].x - tails[tailIndex + 1].x
                val distanceY = tails[tailIndex].y - tails[tailIndex + 1].y
                if (abs(distanceX) >= 2 || abs(distanceY) >= 2) {
                    tails[tailIndex + 1] += Position(distanceX.inRange(-1, 1), distanceY.inRange(-1, 1))
                }
            }
            tailPositions.add(tails.last())
        }
    }

    return tailPositions.size
}

private data class Position(var x: Int, var y: Int) {
    operator fun plus(o: Position) = Position(this.x + o.x, this.y + o.y)
}


