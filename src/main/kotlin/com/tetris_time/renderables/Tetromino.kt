package com.tetris_time.renderables

import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas

abstract class Tetromino(private val fields: List<Field>) : Renderable {
    final override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }

    fun move(direction: MoveDirection) {
        // TODO check if can move

        fields.forEach { it.move(direction) }
    }

    // TODO rotate
}
