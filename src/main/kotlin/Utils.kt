import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

private fun readFileMatrix(day: Int): List<List<String>> =
    readFileLines(day).map { line -> line.split("").filter { it.isNotEmpty() } }

fun readFileIntMatrix(day: Int): List<List<Int>> =
    readFileMatrix(day).map { line -> line.map { it.toInt() } }

fun readFileCharMatrix(day: Int): List<List<Char>> =
    readFileMatrix(day).map { line -> line.map { it.first() } }

fun <T> List<List<T>>.transpose() = List(this[0].size) { i -> List(this.size) { j -> this[j][i] } }
