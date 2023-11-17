package com.tetris_time

import com.tetris_time.enums.MoveDirection
import com.tetris_time.renderables.Map
import com.tetris_time.renderables.Tetromino
import com.tetris_time.renderables.tetrominos.*
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import kotlin.Exception
import kotlin.random.Random

class Game : Application() {

    companion object {
        private const val WIDTH = 10 * 40 + 40 + 4 * 40 // map + gap + next tetromino
        private const val HEIGHT = 20 * 40
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext

    // TODO load images
    // private lateinit var space: Image

    private val map = Map()

    private var currentTetromino: Tetromino = getRandomTetromino().also { it.place() }

    private var nextTetromino: Tetromino = getRandomTetromino()

    private var lastSystemUpdateTime: Long = System.nanoTime()

    private val allowedDirectionKeys = listOf(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN)

    private var latestKey: KeyCode? = null

    private var paused: Boolean = false

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
            if (listOf(*allowedDirectionKeys.toTypedArray(), KeyCode.ESCAPE).contains(event.code)) {
                latestKey = event.code
            }
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
        if (elapsedNanos > 1_000_000_000 && !paused) {
            moveOrPlaceCurrentTetromino(MoveDirection.DOWN)
            lastSystemUpdateTime = currentNanoTime
        }

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        // draw map
        map.render(graphicsContext.canvas)

        // draw next Tetromino
        nextTetromino.render(graphicsContext.canvas)

        // draw current Tetromino
        currentTetromino.render(graphicsContext.canvas)
    }

    private fun performInputUpdate() {
        if (latestKey == null) {
            return
        }

        if (latestKey == KeyCode.ESCAPE) {
            paused = !paused
        } else if (!paused) {
            moveOrPlaceCurrentTetromino(
                when (latestKey) {
                    KeyCode.LEFT -> MoveDirection.LEFT
                    KeyCode.RIGHT -> MoveDirection.RIGHT
                    KeyCode.DOWN -> MoveDirection.DOWN
                    else -> throw Exception("Direction not allowed!")
                }
            )
        }

        latestKey = null
    }

    private fun moveOrPlaceCurrentTetromino(direction: MoveDirection) {
        try {
            currentTetromino.move(direction, map.rowsOfPlacedFields)
        } catch (e: Exception) {
            map.addFieldsOfTetromino(currentTetromino)
            currentTetromino = nextTetromino.also { it.place() }
            nextTetromino = getRandomTetromino()
        }
    }

    private fun getRandomTetromino(): Tetromino {
        val random = Random.nextInt(0, 7)

        return when (random) {
            0 -> IBlock()
            1 -> JBlock()
            2 -> LBlock()
            3 -> OBlock()
            4 -> SBlock()
            5 -> TBlock()
            6 -> ZBlock()
            else -> throw Exception()
        }
    }
}
