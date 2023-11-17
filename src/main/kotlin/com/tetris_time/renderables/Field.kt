package com.tetris_time.renderables

import com.tetris_time.enums.FieldColor
import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas

class Field(
    private var xIndex: Int,
    private var yIndex: Int,
    private val fieldColor: FieldColor = FieldColor.GRAY,
    // TODO offset?
) : Renderable {
    init {
        require(xIndex in 0..<10)
        require(yIndex in 0..<20)
    }

    companion object {
        const val WIDTH = 40.0
        const val HEIGHT = 40.0
    }

    override fun render(canvas: Canvas) {
        val x = WIDTH * xIndex
        val y = HEIGHT * yIndex

        val context = canvas.graphicsContext2D
        context.fill = fieldColor.color
        context.fillRect(x, y, WIDTH, HEIGHT) // TODO display picture of field?
    }

    fun move(direction: MoveDirection) {
        require(xIndex > 0 || direction != MoveDirection.LEFT)
        require(xIndex < 9 || direction != MoveDirection.RIGHT)
        require(yIndex < 19 || direction != MoveDirection.DOWN)

        when(direction) {
            MoveDirection.LEFT -> xIndex--
            MoveDirection.RIGHT -> xIndex++
            MoveDirection.DOWN -> yIndex++
        }
    }
}
