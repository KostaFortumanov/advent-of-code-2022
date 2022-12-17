package day16

import readFileLines
import kotlin.system.measureTimeMillis

fun main() {
    println(part1())
    println(measureTimeMillis { part2() })
}

private fun part1() = traverse(30)

private fun part2() = traverse(26, elephantGoesNext = true)

private fun traverse(
    minutes: Int,
    current: Valve = State.valves["AA"]!!,
    remaining: Set<Valve> = State.closed,
    cache: MutableMap<State, Int> = mutableMapOf(),
    elephantGoesNext: Boolean = false
): Int {
    val currentScore = minutes * current.flow
    val currentState = State(current.name, minutes, remaining)

    return currentScore + cache.getOrPut(currentState) {
        val maxCurrent = remaining
            .filter { next -> State.distances[current.name]!![next.name]!! < minutes }
            .takeIf { it.isNotEmpty() }
            ?.maxOf { next ->
                val remainingMinutes = minutes - 1 - State.distances[current.name]!![next.name]!!
                traverse(remainingMinutes, next, remaining - next, cache, elephantGoesNext)
            }
            ?: 0
        maxOf(maxCurrent, if (elephantGoesNext) traverse(minutes = 26, remaining = remaining) else 0)
    }
}

private data class State(val current: String, val minutes: Int, val opened: Set<Valve>) {
    companion object {
        val valves = readFileLines(16).mapInput()
        val closed = valves.filter { it.value.flow > 0 }.values.toSet()
        val distances = computeDistances(valves)
    }
}

private data class Valve(val name: String, val flow: Int, val children: List<String>)

private fun computeDistances(valves: Map<String, Valve>): Map<String, Map<String, Int>> {
    return valves.keys.map { valve ->
        val distances = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }.apply { put(valve, 0) }
        val toVisit = mutableListOf(valve)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            valves[current]!!.children.forEach { neighbour ->
                val newDistance = distances[current]!! + 1
                if (newDistance < distances.getValue(neighbour)) {
                    distances[neighbour] = newDistance
                    toVisit.add(neighbour)
                }
            }
        }
        distances
    }.associateBy { it.keys.first() }
}

private fun List<String>.mapInput() = this.mapNotNull {
    Regex("Valve ([\\w\\s]+) has flow rate=(\\d+); tunnels? leads? to valves? ([\\w\\s].+)").find(it)?.groupValues
}.associate { gv ->
    gv[1] to Valve(gv[1], gv[2].toInt(), gv[3].split(", "))
}
