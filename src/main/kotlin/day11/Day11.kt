package day11

import readFileLines

fun main() {
    val monkeys1 = readFileLines(11).mapInput()
    println(part1(monkeys1))
    val monkeys2 = readFileLines(11).mapInput()
    println(part2(monkeys2))
}

private fun part1(monkeys: List<Monkey>) = monkeyBusiness(monkeys, 20, true)

private fun part2(monkeys: List<Monkey>): Long = monkeyBusiness(monkeys, 10000, false)

private fun monkeyBusiness(monkeys: List<Monkey>, times: Int, divideByThree: Boolean): Long {
    val initialModulo = if (divideByThree) 3L else 1L
    val modulo = monkeys.map { it.divideBy }.fold(1L) { acc, i -> initialModulo * acc * i }
    repeat(times) {
        for (monkey in monkeys) {
            monkey.inspectItems(modulo, divideByThree)
            monkey.throwItems(monkeys)
        }
    }

    return monkeys
        .sortedBy { it.itemsInspected }
        .takeLast(2)
        .fold(1) { acc, monkey -> acc * monkey.itemsInspected }
}

fun eval(old: Long, expression: String): Long {
    val (first, operator, second) = expression.replace("old", old.toString()).split(" ")
    return when (operator) {
        "+" -> first.toLong() + second.toLong()
        else -> first.toLong() * second.toLong()
    }
}

data class Monkey(
    var items: MutableList<Long>,
    val expression: String,
    val divideBy: Int,
    val throwToMonkey: Pair<Int, Int>,
    var itemsInspected: Long,
) {
    fun inspectItems(modulo: Long, divideByThree: Boolean) {
        itemsInspected += items.size
        items = items.map { eval(it, expression) / (if (divideByThree) 3 else 1) % modulo }.toMutableList()
    }

    fun throwItems(monkeys: List<Monkey>) {
        val first = items.filter { it % divideBy == 0L }
        val second = items.filterNot { it % divideBy == 0L }
        monkeys[throwToMonkey.first].items.addAll(first)
        monkeys[throwToMonkey.second].items.addAll(second)
        items.clear()
    }
}

private fun List<String>.mapInput() = this.chunked(7).map { monkey ->
    val items = monkey[1].numbersInLine().map { it.toLong() }.toMutableList()
    val operation = monkey[2].split("= ")[1]
    val divideBy = monkey[3].numbersInLine().first()
    val throwToMonkey = monkey[4].numbersInLine().first() to monkey[5].numbersInLine().first()
    Monkey(items, operation, divideBy, throwToMonkey, 0)
}

private fun String.numbersInLine() = Regex("[0-9]+").findAll(this).map { it.value.toInt() }.toList()
