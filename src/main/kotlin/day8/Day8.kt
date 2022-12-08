package day8

import readFileIntMatrix
import transpose

fun main() {
    val input = readFileIntMatrix(8)

    println(part1(input))
    println(part2(input))
}

private fun part1(grid: List<List<Int>>) =
    grid.flatMapIndexed { rowIndex, row ->
        row.filterIndexed { colIndex, _ ->
            isVisible(grid, rowIndex, colIndex)
        }
    }.size

private fun part2(grid: List<List<Int>>) =
    grid.flatMapIndexed { rowIndex, row ->
        List(row.size) { colIndex ->
            scenicScore(grid, rowIndex, colIndex)
        }
    }.max()

private fun isVisible(grid: List<List<Int>>, row: Int, col: Int): Boolean {
    val tree = grid[row][col]
    val (left, right) = grid[row].split(col)
    val (top, bottom) = grid.transpose()[col].split(row)

    return tree tallerThanAll left || tree tallerThanAll right || tree tallerThanAll top || tree tallerThanAll bottom
}

private fun List<Int>.split(splitIndex: Int) =
    this.filterIndexed { index, _ -> index < splitIndex } to this.filterIndexed { index, _ -> index > splitIndex }

private infix fun Int.tallerThanAll(list: List<Int>) = list.all { this > it }

private fun scenicScore(grid: List<List<Int>>, row: Int, col: Int): Int {
    val steps = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

    val isValid = { r: Int, c: Int -> r in grid.indices && c in grid[r].indices }

    var scenicScore = 1
    for ((xa, ya) in steps) {
        var newRow = row + xa
        var newCol = col + ya
        var count = 0
        while (isValid(newRow, newCol)) {
            count++
            if (grid[newRow][newCol] >= grid[row][col]) {
                break
            }

            newRow += xa
            newCol += ya
        }
        scenicScore *= count
    }

    return scenicScore
}