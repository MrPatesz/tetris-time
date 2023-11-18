package com.tetris_time.renderables

import javafx.scene.canvas.Canvas

class Row(private var yIndex: Int) : Renderable {
    private var fields: MutableList<Field> = mutableListOf()

    override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }

    fun addField(field: Field) {
        fields.add(field)
    }

    fun isFilled() = fields.size == 10

    fun move(yIndex: Int) {
        require(yIndex in 0..<20)

        this.yIndex = yIndex
        fields.forEach { it.yIndex = yIndex }
    }

    fun isEmpty() = fields.isEmpty()

    fun getYIndex() = yIndex

    fun doesFieldClash(field: Field): Boolean {
        return fields.find { it.xIndex == field.xIndex } != null
    }
}
