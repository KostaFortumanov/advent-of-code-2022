package day14

import readFileLines
import kotlin.math.max
import kotlin.math.min

fun main() {
    val shapes = readFileLines(14).mapInput()

    println(part1(shapes))
    println(part2(shapes))
}

private fun part1(shapes: List<List<Line>>) = unitsOfSand(shapes, false)

private fun part2(shapes: List<List<Line>>) = unitsOfSand(shapes, true)

private fun unitsOfSand(shapes: List<List<Line>>, hasFloor: Boolean) =
    Board(shapes, hasFloor)
        .also { it.pourSand() }
        .unitsOfSand()

private data class Board(val shapes: List<List<Line>>, val hasFloor: Boolean) {

    private val board: List<MutableList<Char>>
    private val sandSource: Point

    init {
        val maxX = shapes.flatten().maxBy { it.maxX }.maxX
        val minX = shapes.flatten().minBy { it.minX }.minX
        sandSource = Point(500 + maxX - minX + 1, 0)

        val scaledShapes = scaleShapes(shapes, minX, maxX)

        val scaledMaxX = scaledShapes.flatten().maxBy { it.maxX }.maxX + maxX
        val scaledMaxY = scaledShapes.flatten().maxBy { it.maxY }.maxY + 3

        board = List(scaledMaxY) { MutableList(scaledMaxX) { '.' } }

        if (hasFloor) {
            drawLine(Line(Point(0, scaledMaxY - 1), Point(scaledMaxX - 1, scaledMaxY - 1)))
        }

        scaledShapes.forEach { line ->
            line.forEach {
                drawLine(it)
            }
        }
        board[sandSource.y][sandSource.x] = '+'
    }

    fun unitsOfSand() = board.flatten().count { it == 'o' }

    fun pourSand() {
        while (true) {
            val steps = listOf(1 to 0, 1 to -1, 1 to 1)
            var newY = sandSource.y
            var newX = sandSource.x

            val isValid = { r: Int, c: Int ->
                r in board.indices && c in board[r].indices && board[r][c] != '#' && board[r][c] != 'o'
            }

            while (true) {
                steps.firstOrNull { (ya, xa) -> isValid(newY + ya, newX + xa) }?.let { (ya, xa) ->
                    newY += ya
                    newX += xa
                } ?: break
            }

            if (hasFloor) {
                if (newY == sandSource.y && newX == sandSource.x) {
                    board[newY][newX] = 'o'
                    break
                }
            } else {
                if (newY == board.lastIndex) {
                    break
                }
            }
            board[newY][newX] = 'o'
        }
    }

    private fun drawLine(line: Line) =
        when (line.lineType) {
            LineType.HORIZONTAL -> drawHorizontalLine(line)
            LineType.VERTICAL -> drawVerticalLine(line)
        }

    private fun drawVerticalLine(line: Line) {
        for (y in line.minY..line.maxY) {
            board[y][line.startPoint.x] = '#'
        }
    }

    private fun drawHorizontalLine(line: Line) {
        for (x in line.minX..line.maxX) {
            board[line.startPoint.y][x] = '#'
        }
    }

    private fun scaleShapes(shapes: List<List<Line>>, minX: Int, maxX: Int) =
        shapes.map { line ->
            line.map {
                Line(
                    Point(it.startPoint.x + maxX - minX + 1, it.startPoint.y),
                    Point(it.endPoint.x + maxX - minX + 1, it.endPoint.y)
                )
            }
        }
}

private data class Line(val startPoint: Point, val endPoint: Point) {

    val minX: Int
        get() = min(startPoint.x, endPoint.x)

    val maxX: Int
        get() = max(startPoint.x, endPoint.x)

    val minY: Int
        get() = min(startPoint.y, endPoint.y)

    val maxY: Int
        get() = max(startPoint.y, endPoint.y)

    val lineType: LineType
        get() = when (startPoint.x) {
            endPoint.x -> LineType.VERTICAL
            else -> LineType.HORIZONTAL
        }
}

private data class Point(val x: Int, val y: Int)

private enum class LineType {
    HORIZONTAL,
    VERTICAL
}

private fun List<String>.mapInput() =
    this.map { line ->
        line.split(" -> ").windowed(2).map { point ->
            val (start, end) = point[0].split(",").map { it.toInt() }
            val (start1, end1) = point[1].split(",").map { it.toInt() }
            Line(Point(start, end), Point(start1, end1))
        }
    }
