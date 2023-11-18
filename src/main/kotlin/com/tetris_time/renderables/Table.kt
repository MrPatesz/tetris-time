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

        renderRect(canvas, x - 1, y - 1, w + 2, (1 + data.size) * ROW_HEIGHT + 2)

        renderRect(canvas, x, y, w, ROW_HEIGHT)
        context.fill = Color.BLACK
        context.fillText(title, x + Field.SIZE * 1.3, y + TEXT_OFFSET)
        y += ROW_HEIGHT
        w /= 2

        data.forEach {
            renderRect(canvas, x, y, w, ROW_HEIGHT)
            context.fill = Color.BLACK
            context.fillText(it.first, x + 5, y + TEXT_OFFSET)

            renderRect(canvas, x + w, y, w, ROW_HEIGHT)
            context.fill = Color.BLACK
            context.fillText(it.second, x + w + 5, y + TEXT_OFFSET)

            y += ROW_HEIGHT
        }
    }

    private fun renderRect(canvas: Canvas, x: Double, y: Double, w: Double, h: Double) {
        val context = canvas.graphicsContext2D
        context.fill = Color.BLACK
        context.fillRect(x, y, w, h)
        context.fill = Color.WHITESMOKE
        context.fillRect(x + 1, y + 1, w - 2, h - 2)
    }

    companion object {
        const val ROW_HEIGHT = 25.0
        const val TEXT_OFFSET = 17
    }
}
