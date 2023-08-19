import org.openrndr.collections.pop
import org.openrndr.collections.push
import org.openrndr.shape.Shape
import org.openrndr.svg.loadSVG
import util.*
import java.io.File

data class Image(val file: File, val shape: Shape) {
    companion object {
        fun fromFile(file: File) = Image(file, loadSVG(file).toShape().scale(10.0))
    }
}

class QueueController {
    val main = ArrayDeque<Image>()
    val completed = ArrayDeque<Image>()
    var normal = true

    fun rename(name: String) {
        if(main.isEmpty()) return

        val current = main.pop()
        completed.push(current)

        current.file.rename(name)
    }

    fun next() {
        if(main.isNotEmpty()) main.next()
    }

    fun previous() {
        if(normal && main.isNotEmpty()) {
            main.previous()
        } else {
            if(completed.isNotEmpty()) main.push(completed.pop())
        }
    }

    fun restart() {
        main.addAll(completed)
        completed.clear()
    }

    fun skip() {
        main.next()
    }

    fun toggleMode() {
        normal = !normal
    }

    fun drop() {
        main.clear()
        completed.clear()
    }
}
