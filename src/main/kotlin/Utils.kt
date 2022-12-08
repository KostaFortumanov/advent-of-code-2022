import java.io.File

fun readFileLines(day: Int): List<String> = File({}.javaClass.getResource("input_$day.txt")!!.path).readLines()

fun readFileIntMatrix(day: Int): List<List<Int>> =
    readFileLines(day).map { line -> line.split("").filter { it.isNotEmpty() }.map { it.toInt() } }

fun <T> List<List<T>>.transpose() = List(this[0].size) { i -> List(this.size) { j -> this[j][i] } }
