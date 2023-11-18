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

    fun placeTetromino(tetromino: Tetromino): Int {
        val numberOfFilledRows = tetromino.addFieldsToRows(rows)

        if(numberOfFilledRows == 0) return 0

        rows = rows.filter { !it.isFilled() }.toMutableList()

        for(i in 0..<numberOfFilledRows){
            rows.add(0, Row(0))
        }

        rows.mapIndexed { index, it -> it.move(index) }

        return numberOfFilledRows
    }

    fun doFieldsClash(fields: List<Field>): Boolean {
        return fields.find {
            val row = rows.find { row -> row.getYIndex() == it.yIndex }
            row!!.doesFieldClash(it)
        } != null
    }

    fun isEmpty() = rows.all { it.isEmpty() }
}