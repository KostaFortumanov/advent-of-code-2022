package day13

import inRange
import org.json.JSONArray
import readFileLines

fun main() {
    val input = readFileLines(13).mapInput()

    println(part1(input))
    println(part2(input))
}

private fun part1(packetPairs: List<Pair<List<Any>, List<Any>>>) =
    packetPairs.foldIndexed(0) { index, acc, (leftPacket, rightPacket) ->
        if (compare(leftPacket, rightPacket) < 0) {
            acc + index + 1
        } else {
            acc
        }
    }

private fun part2(packetPairs: List<Pair<List<Any>, List<Any>>>): Int {
    val firstDivider = listOf(listOf(2))
    val secondDivider = listOf(listOf(6))
    val sorted = (packetPairs.flatMap { it.toList() } + listOf(firstDivider, secondDivider)).sortedWith(::compare)
    return (sorted.indexOf(firstDivider) + 1) * (sorted.indexOf(secondDivider) + 1)
}

private fun compare(leftPacket: List<*>, rightPacket: List<*>): Int =
    leftPacket.zip(rightPacket).map { (left, right) ->
        when {
            left is Int && right is Int -> (left - right).inRange(-1, 1)
            left is Int && right is List<*> -> compare(listOf(left), right)
            left is List<*> && right is Int -> compare(left, listOf(right))
            else -> compare(left as List<*>, right as List<*>)
        }
    }.firstOrNull { it != 0 } ?: (leftPacket.size - rightPacket.size).inRange(-1, 1)

private fun List<String>.mapInput() =
    this.chunked(3).map { (left, right) ->
        parseJsonArray(JSONArray(left)) to parseJsonArray(JSONArray(right))
    }

private fun parseJsonArray(jsonArray: JSONArray): List<Any> =
    jsonArray.mapNotNull {
        when (it) {
            is Int -> it
            is JSONArray -> parseJsonArray(it)
            else -> null
        }
    }
