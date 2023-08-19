package util

import org.openrndr.shape.Composition
import org.openrndr.shape.Shape

fun Composition.toShape(): Shape {
    val shapes = findShapes().flatMap { it.shape.contours }

    return Shape(shapes).adjustForCenter()
}

fun Shape.adjustForCenter() = transform(org.openrndr.math.transforms.transform {
    translate(-bounds.center)
})

fun Shape.scale(scale: Double) = transform(org.openrndr.math.transforms.transform {
    scale(scale)
})
