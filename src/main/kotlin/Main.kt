import org.openrndr.*
import org.openrndr.collections.pop
import org.openrndr.collections.push
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Writer
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.imageFit.fit
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.extra.shapes.grid
import org.openrndr.math.Vector2
import org.openrndr.math.transforms.transform
import org.openrndr.panel.ControlManager
import org.openrndr.panel.controlManager
import org.openrndr.panel.elements.button
import org.openrndr.panel.elements.div
import org.openrndr.panel.elements.requestRedraw
import org.openrndr.panel.elements.textElement
import org.openrndr.panel.layout
import org.openrndr.panel.style.*
import org.openrndr.panel.style.Display
import org.openrndr.panel.styleSheet
import org.openrndr.shape.*
import org.openrndr.svg.loadSVG
import org.openrndr.svg.toSVG
import util.*
import java.io.File

fun main() = application {
    configure {
        width = 900
        height = 900
        windowResizable = true
        title = "re-svg"
        vsync = true
    }
    program {
        val queue = QueueController()

        window.drop.listen {
            if (queue.main.isEmpty()) queue.drop()

            val images = it.files.map { Image.fromFile(File(it)) }

            queue.main.addAll(images)
        }

        keyboard.keyDown.listen {
            when(it.key) {
                KEY_ARROW_LEFT -> queue.previous()
                KEY_ARROW_RIGHT -> queue.next()
                KEY_ESCAPE -> queue.drop()
                KEY_ARROW_UP -> queue.restart()
                KEY_ARROW_DOWN -> queue.toggleMode()
                KEY_SPACEBAR -> queue.skip()
                else -> {
                    val name = when(it.name) {
                        "-" -> "dash"
                        "." -> "period"
                        "," -> "comma"
                        else -> it.name
                    }

                    queue.rename("$name.svg")
                }
            }
        }

        extend {
            drawer.fill = ColorRGBa.BLACK
            drawer.clear(ColorRGBa.WHITE)

            writer {
                box = Rectangle(10.0, 20.0, 600.0, 300.0)
                text("""
                    Normal Mode: ${queue.normal}
                    Images Left: ${queue.main.size}
                    Images Completed: ${queue.completed.size}
                """.trimIndent())
            }

            drawer.translate(drawer.bounds.position(0.5, 0.5))

            queue.main.lastOrNull()?.let {
                drawer.shape(it.shape)
            }
        }
    }
}
