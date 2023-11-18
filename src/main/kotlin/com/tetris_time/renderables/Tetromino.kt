package com.tetris_time.renderables

import com.tetris_time.enums.MoveDirection
import javafx.scene.canvas.Canvas

abstract class Tetromino(private var fields: List<Field>) : Renderable {
    init {
        fields.forEach { it.xIndex += NEXT_X_OFFSET }
    }

    final override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }

    fun getMovedFields(direction: MoveDirection): List<Field> {
        return fields.map { it.copy().apply { move(direction) } }
    }

    fun setFields(newFields: List<Field>) {
        fields = newFields
    }

    fun addFieldsToRows(rows: MutableList<Row>): List<Row> {
        fields.forEach {
            val row = rows.find { row -> row.getYIndex() == it.yIndex }
            row!!.addField(it)
        }

        return rows.filter { it.isFilled() }
    }

    fun place() {
        fields.forEach { it.xIndex -= NEXT_X_OFFSET }
    }

    companion object {
        const val NEXT_X_OFFSET = 8
    }
}
