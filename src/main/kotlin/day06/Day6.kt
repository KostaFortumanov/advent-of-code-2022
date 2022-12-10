package day06

import readFileLines

fun main() {
    val input = readFileLines(6).first()

    println(part1(input))
    println(part2(input))
}

private fun part1(packet: String): Int = packetSequence(packet, 4)

private fun part2(packet: String): Int = packetSequence(packet, 14)

private fun packetSequence(packet: String, messageSize: Int): Int =
    packet.windowed(messageSize)
        .asSequence()
        .map { it.toSet() }
        .indexOfFirst { it.size == messageSize } + messageSize
