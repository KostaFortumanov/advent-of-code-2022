package day5

import readFileLines

fun main() {
    val (crateString, movesString) = readFileLines("input_5.txt")
        .joinToString("\n")
        .split("\n\n")

    val crates = cratesFromCrateString(crateString)
    val moves = movesFromMovesString(movesString)

    println(part1(crates, moves))
    println(part2(crates, moves))
}

private fun part1(crates: List<List<Char>>, moves: List<Move>): String {
    val stacks = crates.map { it.toMutableList() }.toMutableList()
    moves.forEach {
        val toMove = stacks[it.from].takeLast(it.amount).reversed()
        stacks[it.to].addAll(toMove)
        stacks[it.from] = stacks[it.from].dropLast(it.amount).toMutableList()
    }

    return stacks.map { it.last() }.joinToString("")
}

private fun part2(crates: List<List<Char>>, moves: List<Move>): String {
    val stacks = crates.map { it.toMutableList() }.toMutableList()

    moves.forEach {
        val toMove = stacks[it.from].takeLast(it.amount)
        stacks[it.to].addAll(toMove)
        stacks[it.from] = stacks[it.from].dropLast(it.amount).toMutableList()
    }

    return stacks.map { it.last() }.joinToString("")
}

private data class Move(val amount: Int, val from: Int, val to: Int)

private fun cratesFromCrateString(crateString: String): List<List<Char>> {
    val width = crateString.lines().maxOf { it.length }
    return crateString.lines()
        .dropLast(1)
        .map {
            it.padEnd(width, ' ')
                .slice(1 until width step 4)
                .toList()
        }
        .transpose()
        .map { it.filter { char -> !char.isWhitespace() }.reversed() }
}

private fun movesFromMovesString(movesString: String) =
    movesString.lines().map { line ->
        Regex("[0-9]+").findAll(line)
            .map { it.value.toInt() }
            .toList()
    }.map { (amount, from, to) -> Move(amount, from - 1, to - 1) }


private fun List<List<Char>>.transpose() = List(this[0].size) { i -> List(this.size) { j -> this[j][i] } }
