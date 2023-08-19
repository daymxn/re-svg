package util

import org.openrndr.collections.pop
import org.openrndr.collections.push

fun <T> ArrayDeque<T>.next() = pop().also { addFirst(it) }
fun <T> ArrayDeque<T>.previous() = removeFirst().also { push(it) }
