import java.io.File

fun readFileLines(fileName: String): List<String> = File({}.javaClass.getResource(fileName)!!.path).readLines()
