package com.tetris_time.renderables

import com.tetris_time.enums.FieldColor
import com.tetris_time.enums.MoveDirection
import com.tetris_time.getRandomTetromino
import javafx.scene.canvas.Canvas

class Map : Renderable {
    private val rows = Rows()
    private var currentTetromino: Tetromino = getRandomTetromino().also { it.place() }
    private var nextTetromino: Tetromino = getRandomTetromino()

    override fun render(canvas: Canvas) {
        // draw background
        backgroundFields.forEach { it.render(canvas) }

        // draw placed fields
        rows.render(canvas)

        // draw current Tetromino
        currentTetromino.render(canvas)

        // draw next Tetromino
        nextTetromino.render(canvas)
    }

    fun moveCurrentTetromino(direction: MoveDirection) {
        val newFields = currentTetromino.getMovedFields(direction)

        val fieldOutOfBounds = newFields.find {it.xIndex !in 0..<10 || it.yIndex !in 0..<20 }

        val cannotMove = fieldOutOfBounds != null || rows.doFieldsClash(newFields)

        if (cannotMove) {
            if (direction == MoveDirection.DOWN) {
                rows.placeTetromino(currentTetromino)
                currentTetromino = nextTetromino.also { it.place() }
                nextTetromino = getRandomTetromino()
            }
        } else {
            currentTetromino.setFields(newFields)
        }
    }

    companion object {
        private val backgroundFields: List<Field> = mutableListOf<Field>().run {
            for (i in 0..<10) {
                for (j in 0..<20) {
                    add(Field(xIndex = i, yIndex = j, fieldColor = FieldColor.GRAY))
                }
            }
            toList()
        }
    }
}