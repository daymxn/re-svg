package util

import java.io.File

val File.directory: String
    get() = absolutePath.substringBeforeLast(name)

fun File.rename(name: String) = renameTo(File("$directory/$name"))
