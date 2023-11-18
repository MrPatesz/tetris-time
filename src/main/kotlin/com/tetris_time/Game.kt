package com.tetris_time

import com.tetris_time.enums.MoveDirection
import com.tetris_time.renderables.Field
import com.tetris_time.renderables.Map
import com.tetris_time.renderables.Table
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import java.io.File

class Game : Application() {

    companion object {
        private const val WIDTH = 10 * Field.SIZE + Field.SIZE + 4 * Field.SIZE + Field.SIZE // map + gap + panel + gap
        private const val HEIGHT = 20 * Field.SIZE
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext

    // TODO load images
    // private lateinit var space: Image

    private var highScores: MutableList<Pair<String, String>> = mutableListOf()

    private val highScoresTable: Table = Table("HighScores", highScores, 11, 5)

    private val legend: Table = Table(
        "Legend", listOf(
            Pair("Enter", "Start"),
            Pair("Escape", "Stop/Resume"),
            Pair("Space", "Rotate"),
            Pair("Arrows", "Move"),
        ), 11, 13
    )

    private var map = Map { started = false }

    private var lastSystemUpdateTime: Long = System.nanoTime()

    private val allowedDirectionKeys = listOf(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN)

    private var latestKey: KeyCode? = null

    private var paused: Boolean = false

    private var started = false

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
        loadHighScores()

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
            if (listOf(*allowedDirectionKeys.toTypedArray(), KeyCode.ESCAPE, KeyCode.SPACE, KeyCode.ENTER).contains(
                    event.code
                )
            ) {
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

    private fun loadHighScores() {
        var place = 1

        val filePath = getResource("/highscores.txt").drop(6)
        File(filePath).forEachLine {
            val strings = it.split(';')
            val name = strings[0]
            val score = strings[1]
            highScores.add(Pair("${place++}. $name", score))
        }

        while (highScores.size != 10) {
            highScores.add(Pair("${highScores.size + 1}.", ""))
        }
    }

    private fun tickAndRender(currentNanoTime: Long) {
        // the time elapsed since the last frame, in nanoseconds
        // can be used for physics calculation, etc
        val elapsedNanos = currentNanoTime - lastSystemUpdateTime

        // perform world updates
        performInputUpdate()
        if (elapsedNanos > 1_000_000_000 && !paused && started) {
            map.moveCurrentTetromino(MoveDirection.DOWN)
            lastSystemUpdateTime = currentNanoTime
        }

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        // draw map
        map.render(graphicsContext.canvas)

        // draw scoreboard
        highScoresTable.render(graphicsContext.canvas)

        // draw legend
        legend.render(graphicsContext.canvas)
    }

    private fun performInputUpdate() {
        when (latestKey) {
            null -> return
            KeyCode.ENTER -> {
                if(!map.isMapEmpty()) {
                    map = Map { started = false }
                }
                started = true
            }

            KeyCode.ESCAPE -> {
                if (started) {
                    paused = !paused
                }
            }

            else -> {
                if (paused || !started) {
                    return
                }

                if (latestKey == KeyCode.SPACE) {
                    map.rotateCurrentTetromino()
                } else {
                    map.moveCurrentTetromino(
                        when (latestKey) {
                            KeyCode.LEFT -> MoveDirection.LEFT
                            KeyCode.RIGHT -> MoveDirection.RIGHT
                            KeyCode.DOWN -> MoveDirection.DOWN
                            else -> throw Exception("Direction not allowed!")
                        }
                    )
                }
            }
        }
        latestKey = null
    }
}
