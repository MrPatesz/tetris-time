package com.tetris_time.renderables

import com.tetris_time.enums.FieldColor
import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas
import javafx.scene.effect.Lighting
import javafx.scene.paint.Color

data class Field(
    var xIndex: Int,
    var yIndex: Int,
    private val fieldColor: FieldColor,
) : Renderable {
    override fun render(canvas: Canvas) {
        val x = SIZE * xIndex
        val y = SIZE * yIndex

        val context = canvas.graphicsContext2D
        context.fill = Color.BLACK
        context.fillRect(x, y, SIZE, SIZE)

        val shadow = Lighting()
        shadow.diffuseConstant = 1.5
        context.setEffect(shadow)
        context.fill = fieldColor.color
        context.fillRect(x + 1, y + 1, SIZE - 2, SIZE - 2)
        context.setEffect(null)
    }

    fun move(direction: MoveDirection) {
        when (direction) {
            MoveDirection.LEFT -> xIndex--
            MoveDirection.RIGHT -> xIndex++
            MoveDirection.DOWN -> yIndex++
        }
    }

    fun clashes(otherField: Field): Boolean {
        return xIndex == otherField.xIndex && yIndex == otherField.yIndex
    }

    companion object {
        const val SIZE = 40.0
    }
}
