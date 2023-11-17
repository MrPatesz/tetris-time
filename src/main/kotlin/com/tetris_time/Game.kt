package com.tetris_time

import com.tetris_time.enums.MoveDirection
import com.tetris_time.renderables.Map
import com.tetris_time.renderables.tetrominos.IBlock
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.stage.Stage

class Game : Application() {

    companion object {
        private const val WIDTH = 10 * 40
        private const val HEIGHT = 20 * 40
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext

    // TODO load images
    // private lateinit var space: Image

    private val currentTetromino = IBlock() // TODO getNextTetromino() { create random }

    private var lastSystemUpdateTime: Long = System.nanoTime()

    private var latestKey: KeyCode? = null

    override fun start(mainStage: Stage) {
        mainStage.title = "Tetris Time"

        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        root.children.add(canvas)

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D

        loadGraphics()

        // Main loop
        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            latestKey = event.code
        }
    }

    private fun loadGraphics() {
        // prefixed with / to indicate that the files are
        // in the root of the "resources" folder
        // space = Image(getResource("/space.png"))
        // sun = Image(getResource("/sun.png"))
    }

    private fun tickAndRender(currentNanoTime: Long) {
        // the time elapsed since the last frame, in nanoseconds
        // can be used for physics calculation, etc
        val elapsedNanos = currentNanoTime - lastSystemUpdateTime

        // perform world updates
        performInputUpdate()
        if (elapsedNanos > 1_000_000_000) {
            currentTetromino.move(MoveDirection.DOWN)
            lastSystemUpdateTime = currentNanoTime
        }

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        // draw background
        Map().render(graphicsContext.canvas)

        // draw rows
        // TODO

        // draw current Tetromino
        currentTetromino.render(graphicsContext.canvas)
    }

    private fun performInputUpdate() {
        if (latestKey == null) {
            return
        }

        if (latestKey == KeyCode.LEFT) {
            currentTetromino.move(MoveDirection.LEFT)
        }
        if (latestKey == KeyCode.RIGHT) {
            currentTetromino.move(MoveDirection.RIGHT)
        }
        if (latestKey == KeyCode.DOWN) {
            currentTetromino.move(MoveDirection.DOWN)
        }

        latestKey = null
    }
}
