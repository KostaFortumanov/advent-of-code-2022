package day7

import readFileLines

fun main() {
    val input = readFileLines(7).mapInput()
    val rootDirectory = generateFileSystemTree(input)
    val sizes = getDirectorySizes(rootDirectory)

    println(part1(sizes))
    println(part2(sizes))
}

private fun part1(sizes: List<Int>): Int = sizes.filter { it <= 100000 }.sum()

private fun part2(sizes: List<Int>): Int {
    val usedSpace = sizes.max()
    val fileSizeToDelete = 30000000 - 70000000 + usedSpace
    return sizes.filter { it >= fileSizeToDelete }.min()
}

private fun getDirectorySizes(rootDirectory: Directory): List<Int> =
    rootDirectory.directories.fold(mutableListOf<Int>()) { acc, directory ->
        acc.addAll(getDirectorySizes(directory))
        acc
    } + rootDirectory.size

private fun generateFileSystemTree(input: List<String>): Directory {
    val root = Directory("/")
    var currentDirectory = root
    input.forEach { line ->
        val (command, arg) = line.split(" ")
        when (command) {
            "cd" -> currentDirectory = when (arg) {
                ".." -> currentDirectory.parent!!
                else -> currentDirectory.findDirectoryByName(arg)
            }

            "dir" -> currentDirectory.addDirectory(arg)
            else -> currentDirectory.addFile(command.toInt())
        }
    }

    return root
}

private data class Directory(
    val name: String,
    val parent: Directory? = null,
    private var fileSize: Int = 0,
    private val _directories: MutableList<Directory> = mutableListOf(),
) {
    val directories: List<Directory>
        get() = _directories

    val size: Int
        get() = fileSize + directories.sumOf { it.size }

    fun addDirectory(name: String) {
        _directories.add(Directory(name, this))
    }

    fun addFile(size: Int) {
        fileSize += size
    }

    fun findDirectoryByName(name: String) = directories.first { it.name == name }
}

private fun List<String>.mapInput() =
    this.map { it.replace("$ ", "") }
        .filter { it != "ls" }
        .drop(1)
