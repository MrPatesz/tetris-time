package com.tetris_time.renderables

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color

class Table(
    private val title: String,
    private val data: List<Pair<String, String>>,
    private val xIndex: Int,
    private val yIndex: Int,
) : Renderable {
    override fun render(canvas: Canvas) {
        val context = canvas.graphicsContext2D

        val x = Field.SIZE * xIndex
        var y = Field.SIZE * yIndex
        var w = 4 * Field.SIZE

        fun renderRect(_x: Double, _y: Double, _w: Double, _h: Double) {
            context.fill = Color.BLACK
            context.fillRect(_x, _y, _w, _h)
            context.fill = Color.WHITE
            context.fillRect(_x + 1, _y + 1, _w - 2, _h - 2)
        }

        renderRect(x, y, w, _h = ROW_HEIGHT)
        context.fill = Color.BLACK
        context.fillText(title, x + Field.SIZE * 1.3, y + TEXT_OFFSET)
        y += ROW_HEIGHT
        w /= 2

        data.forEach {
            renderRect(x, y, w, ROW_HEIGHT)
            renderRect(x + w, y, w, ROW_HEIGHT)
            context.fill = Color.BLACK
            context.fillText(it.first, x + 5, y + TEXT_OFFSET)
            context.fillText(it.second, x + w + 5, y + TEXT_OFFSET)
            y += ROW_HEIGHT
        }
    }

    companion object {
        const val ROW_HEIGHT = 25.0
        const val TEXT_OFFSET = 17
    }
}
