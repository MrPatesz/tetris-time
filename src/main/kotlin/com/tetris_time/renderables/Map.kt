package com.tetris_time.renderables

import javafx.scene.canvas.Canvas

class Map : Renderable {

    val rowsOfPlacedFields: MutableList<MutableList<Field>> = mutableListOf<MutableList<Field>>().apply {
        for (i in 0..<20){
            add(mutableListOf())
        }
    }

    companion object {
        private val fields: List<Field> = mutableListOf<Field>().run {
            for (i in 0..<10) {
                for (j in 0..<20) {
                    add(Field(xIndex = i, yIndex = j))
                }
            }
            toList()
        }
    }

    override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }

        rowsOfPlacedFields.forEach { row -> row.forEach { it.render(canvas) } }
    }

    fun addFieldsOfTetromino(tetromino: Tetromino) {
        tetromino.getFields().forEach{ rowsOfPlacedFields[it.yIndex].add(it) }
    }
}