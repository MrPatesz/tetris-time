package com.tetris_time.renderables

import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas

abstract class Tetromino(private var fields: List<Field>) : Renderable {
    init {
        fields.forEach { it.xOffset = 8 }
    }

    final override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }

    fun move(direction: MoveDirection, rowsOfPlacedFields: MutableList<MutableList<Field>>) {
        require(fields.all { it.xOffset == 0 })
        try {
            val newFields = fields.map { it.copy() }
            newFields.forEach { it.move(direction) }

            newFields.forEach { field ->
                val xd = rowsOfPlacedFields[field.yIndex].find { field.xIndex == it.xIndex }
                if (xd != null) {
                    throw Exception("Place Tetromino!")
                }
            }

            fields = newFields
        } catch (e: Exception) {
            check(e.message != "Cannot go further down!")
            check(e.message != "Place Tetromino!")
        }
    }

    private fun canMove(direction: MoveDirection, rowsOfPlacedFields: MutableList<MutableList<Field>>){

    }

    // TODO rotate() {}

    fun place() {
        // TODO throw Exception if cannot place
        fields.forEach { it.xOffset = 0 }
    }

    fun getFields() = fields
}
