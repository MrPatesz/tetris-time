package com.tetris_time.renderables

import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas

abstract class Tetromino(private var fields: List<Field>) : Renderable {
    final override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }

    fun move(direction: MoveDirection): Boolean {
        // TODO check if can move

        return try {
            val newFields = fields.map { it.copy() }
            newFields.forEach { it.move(direction) }

            fields = newFields
            true
        } catch (e: Exception) {
            check(e.message != "Cannot go further down!")
            false
        }
    }

    // TODO rotate() {}

    // TODO canPlace() {}
}
