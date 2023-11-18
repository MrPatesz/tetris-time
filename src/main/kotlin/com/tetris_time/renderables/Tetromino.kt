package com.tetris_time.renderables

import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas

abstract class Tetromino(var fields: List<Field>) : Renderable {
    init {
        fields.forEach {
            it.xIndex += NEXT_X_OFFSET
            it.yIndex += NEXT_Y_OFFSET
        }
    }

    final override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }

    open fun getRotatedFields(): List<Field> {
        val minXIndex = fields.minOf { it.xIndex }
        val minYIndex = fields.minOf { it.yIndex }
        val xIndices = fields.map { it.xIndex - minXIndex }
        val yIndices = fields.map { it.yIndex - minYIndex }

        val centerX = xIndices[1]
        val centerY = yIndices[1]

        return fields.mapIndexed { index, it ->
            it.copy().apply {
                xIndex = -(yIndices[index] - centerY) + centerX + minXIndex
                yIndex = xIndices[index] - centerX + centerY + minYIndex
            }
        }
    }

    fun getMovedFields(direction: MoveDirection): List<Field> {
        return fields.map { it.copy().apply { move(direction) } }
    }

    fun addFieldsToRows(rows: MutableList<Row>): Int {
        fields.forEach {
            val row = rows.find { row -> row.getYIndex() == it.yIndex }
            row!!.addField(it)
        }

        return rows.count { it.isFilled() }
    }

    fun place() {
        fields.forEach {
            it.xIndex -= NEXT_X_OFFSET
            it.yIndex -= NEXT_Y_OFFSET
        }
    }

    companion object {
        const val NEXT_X_OFFSET = 8
        const val NEXT_Y_OFFSET = 1
    }
}
