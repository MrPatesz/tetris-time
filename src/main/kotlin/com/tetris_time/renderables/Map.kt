package com.tetris_time.renderables

import com.tetris_time.enums.FieldColor
import com.tetris_time.enums.MoveDirection
import com.tetris_time.getRandomTetromino
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlin.math.pow

class Map(private val onRoundEnd: () -> Unit, private val onScoreChange: (Int) -> Unit) : Renderable {
    private val rows = Rows()
    private var currentTetromino: Tetromino = getRandomTetromino().also { it.place() }
    private var nextTetromino: Tetromino = getRandomTetromino()
    private var score = 0

    fun getScore() = score

    override fun render(canvas: Canvas) {
        val context = canvas.graphicsContext2D

        // draw background
        backgroundFields.forEach { it.render(canvas) }

        // draw placed fields
        rows.render(canvas)

        // draw current Tetromino
        currentTetromino.render(canvas)

        // draw next Tetromino
        context.fill = Color.BLACK
        context.fillRect(11 * Field.SIZE, Field.SIZE, 4 * Field.SIZE, 2 * Field.SIZE)
        context.fill = Color.WHITE
        context.fillRect(11 * Field.SIZE + 1, Field.SIZE + 1.0, 4 * Field.SIZE - 2, 2 * Field.SIZE - 2)
        nextTetromino.render(canvas)

        // draw score
        context.fill = Color.BLACK
        context.fillText("You have $score points!", 11 * Field.SIZE, 4 * Field.SIZE)
    }

    fun moveCurrentTetromino(direction: MoveDirection) {
        val newFields = currentTetromino.getMovedFields(direction)

        if (moveNotAllowed(newFields)) {
            if (direction == MoveDirection.DOWN) {
                score += rows.placeTetromino(currentTetromino).toDouble().pow(2).toInt()
                if (score != 0) {
                    onScoreChange(score)
                }

                currentTetromino = nextTetromino.also {
                    it.place()
                    if (moveNotAllowed(it.fields)) {
                        onRoundEnd()
                    }
                }
                nextTetromino = getRandomTetromino()
            }
        } else {
            currentTetromino.fields = newFields
        }
    }

    fun rotateCurrentTetromino() {
        val newFields = currentTetromino.getRotatedFields()

        if (!moveNotAllowed(newFields)) {
            currentTetromino.fields = newFields
        }
    }

    private fun moveNotAllowed(newFields: List<Field>): Boolean {
        val fieldOutOfBounds = newFields.find { it.xIndex !in 0..<10 || it.yIndex !in 0..<20 }

        return fieldOutOfBounds != null || rows.doFieldsClash(newFields)
    }

    fun isMapEmpty() = rows.isEmpty()

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