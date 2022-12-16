package day15

import readFileLines
import kotlin.math.abs

fun main() {
    val scannerBeacons = readFileLines(15).mapInput()

    println(part1(scannerBeacons))
    println(part2(scannerBeacons))
}

private fun part1(scannerBeacons: List<Pair<Point, Point>>) = positionsNoBeacon(scannerBeacons).size

private fun part2(scannerBeacons: List<Pair<Point, Point>>) = tuningFrequency(scannerBeacons)

private fun tuningFrequency(scannerBeacons: List<Pair<Point, Point>>, maxPoint: Int = 4000000): Long {
    val scannerDistances = scannerBeacons.associate { (scanner, beacon) ->
        scanner to scanner.distanceTo(beacon)
    }

    val boundaryLines = scannerDistances.entries.flatMap { (scanner, distance) ->
        scanner.boundaryLines(distance + 1)
    }

    val crossingPoints = boundaryLines
        .flatMap { line1 -> boundaryLines.mapNotNull { line2 -> getCrossingPoint(line1, line2) } }
        .filter { (x, y) -> x in (0..maxPoint) && y in (0..maxPoint) }
        .distinct()

    val beacon = crossingPoints.first { point ->
        scannerDistances.all { (scanner, distance) -> scanner.distanceTo(point) > distance }
    }

    return beacon.x * 4000000L + beacon.y
}

private fun getCrossingPoint(line1: Pair<Point, Point>, line2: Pair<Point, Point>): Point? {
    val c1 = run {
        val line1c1 = line1.first.x + line1.first.y
        val line1c2 = line1.second.x + line1.second.y
        val line2c1 = line2.first.x + line2.first.y
        val line2c2 = line2.second.x + line2.second.y
        when {
            line1c1 == line1c2 -> line1c1
            line2c1 == line2c2 -> line2c1
            else -> return null
        }
    }

    val c2 = run {
        val line1c1 = line1.first.x - line1.first.y
        val line1c2 = line1.second.x - line1.second.y
        val line2c1 = line2.first.x - line2.first.y
        val line2c2 = line2.second.x - line2.second.y
        when {
            line1c1 == line1c2 -> line1c1
            line2c1 == line2c2 -> line2c1
            else -> return null
        }
    }

    return Point((c1 + c2) / 2, (c1 - c2) / 2)
}

private fun positionsNoBeacon(input: List<Pair<Point, Point>>, row: Int = 2000000) =
    input.filter { (sensor, beacon) ->
        val distance = sensor.distanceTo(beacon)
        abs(sensor.y - row) <= distance
    }.flatMap { (sensor, beacon) ->
        val distance = sensor.distanceTo(beacon)
        val startX = abs(sensor.y - row) - distance + sensor.x
        val endX = distance - abs(sensor.y - row) + sensor.x

        (startX..endX).zip(List(endX - startX + 1) { row }).map { (x, y) -> Point(x, y) } - beacon
    }.toSet()

data class Point(val x: Int, val y: Int) {

    fun distanceTo(other: Point): Int = abs(x - other.x) + abs(y - other.y)

    fun boundaryLines(distance: Int): List<Pair<Point, Point>> {
        val up = copy(y = y - distance)
        val down = copy(y = y + distance)
        val left = copy(x = x - distance)
        val right = copy(x = x + distance)
        return listOf(up to left, up to right, down to left, down to right)
    }
}

private fun List<String>.mapInput() = this.map { line ->
    val (x1, y1, x2, y2) = Regex("-?[0-9]+").findAll(line).map { it.value.toInt() }.toList()
    Point(x1, y1) to Point(x2, y2)
}
