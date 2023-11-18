package com.tetris_time.renderables

import javafx.scene.canvas.Canvas

class Rows : Renderable {
    private var rows: MutableList<Row> = mutableListOf<Row>().apply {
        for (i in 0..<20) {
            add(Row(i))
        }
    }

    override fun render(canvas: Canvas) {
        rows.forEach { it.render(canvas) }
    }

    fun placeTetromino(tetromino: Tetromino) {
        val filledRows = tetromino.addFieldsToRows(rows)

        val numberOfFilledRows = filledRows.size // TODO calculate points

        if(numberOfFilledRows == 0) return

        val minIndexOfFilledRows = filledRows.minOf { it.getYIndex() }

        // moving rows down above filled rows
        rows.forEach {
            if(it.getYIndex() < minIndexOfFilledRows){
                it.move(it.getYIndex() + numberOfFilledRows)
            }
        }

        // clearing and moving filled rows
        for(i in 0..<numberOfFilledRows) {
            rows.find { it.isFilled() }?.apply {
                clear()
                move(i)
            }
        }
    }

    fun doFieldsClash(fields: List<Field>): Boolean {
        return fields.find {
            val row = rows.find { row -> row.getYIndex() == it.yIndex }
            row!!.doesFieldClash(it)
        } != null
    }
}