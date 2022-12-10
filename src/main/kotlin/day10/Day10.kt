package day10

import readFileLines

fun main() {
    val input = readFileLines(10).mapInput()

    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Pair<String, Int>>): Int {
    val cycles = List(6) { i -> i * 40 + 20 }
    val signalStrengths = mutableListOf<Int>()
    var xRegister = 1
    var currCycle = 1
    input.forEach { line ->
        val (instruction, value) = line
        when (instruction) {
            "addx" -> {
                repeat(2) {
                    addSignalStrength(signalStrengths, cycles, currCycle, xRegister)
                    currCycle++
                }
                xRegister += value
            }

            else -> {
                addSignalStrength(signalStrengths, cycles, currCycle, xRegister)
                currCycle++
            }
        }
    }

    return signalStrengths.sum()
}

private fun part2(input: List<Pair<String, Int>>): String {
    val pixels = mutableListOf<String>()
    var xRegister = 1
    var currCycle = 0
    input.forEach { line ->
        val (instruction, value) = line
        when (instruction) {
            "addx" -> {
                repeat(2) {
                    addPixel(pixels, currCycle, xRegister)
                    currCycle++
                }
                xRegister += value
            }

            else -> {
                addPixel(pixels, currCycle, xRegister)
                currCycle++
            }
        }
    }

    return pixels.chunked(40).joinToString("\n") { it.joinToString("") }
}

private fun addSignalStrength(signalStrengths: MutableList<Int>, cycles: List<Int>, currCycle: Int, xRegister: Int) {
    if (currCycle in cycles) {
        signalStrengths.add(currCycle * xRegister)
    }
}

private fun addPixel(pixels: MutableList<String>, currCycle: Int, xRegister: Int) {
    if (currCycle % 40 in xRegister - 1..xRegister + 1) {
        pixels.add("#")
    } else {
        pixels.add(".")
    }
}

private fun List<String>.mapInput(): List<Pair<String, Int>> = this.map {
    val parts = it.split(" ")
    parts[0] to (parts.getOrNull(1)?.toInt() ?: 0)
}
