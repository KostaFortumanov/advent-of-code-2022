package day12

import readFileCharMatrix
import java.util.*

fun main() {
    val input = readFileCharMatrix(12)

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<List<Char>>) = minDistance(input) { it == 'S' }

private fun part2(input: List<List<Char>>) = minDistance(input) { it == 'S' || it == 'a' }

private fun minDistance(input: List<List<Char>>, startPosition: (c: Char) -> Boolean) =
    positions(input, startPosition)
        .map { (startRow, startCol) -> distance(input, startRow, startCol) }
        .filter { it != 0 }
        .min()

private fun distance(input: List<List<Char>>, startRow: Int, startCol: Int): Int {
    val grid = input.map { line -> line.map { Position(it) } }
    val steps = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    val isValid = { row: Int, col: Int, currPosition: Position ->
        row in grid.indices && col in grid[row].indices &&
                !grid[row][col].visited && currPosition.canGoToPosition(grid[row][col])
    }

    val queue = LinkedList<Pair<Int, Int>>()
    queue.add(startRow to startCol)
    grid[startRow][startCol].visit(0)

    while (queue.isNotEmpty()) {
        val (currRow, currCol) = queue.pop()
        steps.forEach { (dx, dy) ->
            val newRow = currRow + dx
            val newCol = currCol + dy
            if (isValid(newRow, newCol, grid[currRow][currCol])) {
                grid[newRow][newCol].visit(grid[currRow][currCol].distance + 1)
                queue.add(newRow to newCol)
            }
        }
    }

    val (endRow, endCol) = positions(input) { it == 'E' }.first()
    return grid[endRow][endCol].distance
}

private fun positions(input: List<List<Char>>, position: (c: Char) -> Boolean) =
    input.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, char ->
            if (position(char)) {
                rowIndex to colIndex
            } else {
                null
            }
        }
    }

private data class Position(val value: Char, private var _visited: Boolean = false, private var _distance: Int = 0) {
    val visited: Boolean
        get() = _visited

    val distance: Int
        get() = _distance

    val code: Int
        get() = when (value) {
            'S' -> 'a'
            'E' -> 'z'
            else -> value
        }.code

    fun visit(distance: Int) {
        _visited = true
        _distance = distance
    }

    fun canGoToPosition(other: Position) = other.code - code <= 1
}
