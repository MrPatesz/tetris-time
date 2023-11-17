package com.tetris_time.renderables

import javafx.scene.canvas.Canvas

class Map : Renderable {
    companion object {
        private val fields: List<Field> = mutableListOf<Field>().run {
            for(i in 0..< 10) {
                for(j in 0..< 20) {
                    add(Field(xIndex = i, yIndex = j))
                }
            }
            toList()
        }
    }

    override fun render(canvas: Canvas) {
        fields.forEach { it.render(canvas) }
    }
}