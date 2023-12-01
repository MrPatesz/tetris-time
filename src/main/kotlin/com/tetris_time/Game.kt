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
import javafx.scene.control.TextInputDialog
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.File
import kotlin.math.sqrt

class Game : Application() {

    companion object {
        private const val WIDTH = 10 * Field.SIZE + Field.SIZE + 4 * Field.SIZE + Field.SIZE // map + gap + panel + gap
        private const val HEIGHT = 20 * Field.SIZE
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext

    private var highScores: MutableList<Pair<String, String>> = mutableListOf()

    private val highScoresTable: Table = Table("Scoreboard", highScores, 11, 5)

    private val legend: Table = Table(
        "Controls", listOf(
            Pair("Enter", "Start"),
            Pair("Escape", "Stop/Resume"),
            Pair("Space", "Rotate"),
            Pair("Arrows", "Move"),
        ), 11, 13
    )

    private var map = Map(::onRoundEnd, ::onScoreChange)

    private var systemUpdateInterval = 1_000_000_000
    private var lastSystemUpdateTime: Long = System.nanoTime()

    private val allowedDirectionKeys = listOf(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.DOWN)

    private var latestKey: KeyCode? = null

    private var paused: Boolean = false

    private var started = false

    private fun onScoreChange(newScore: Int) {
        systemUpdateInterval = 1_000_000_000 / sqrt(sqrt(newScore.toDouble())).toInt()
    }

    private fun onRoundEnd() {
        started = false
        val score = map.getScore().toString()
        val placement = highScores.indexOfFirst { it.second < score }

        if (placement != -1) {
            val dialog = TextInputDialog().apply {
                title = "Congratulations!"
                headerText = "How would you like to appear on the Scoreboard?"
                contentText = "Name:"
            }

            dialog.setOnHidden {
                val name = dialog.result?.filter { it != ';' && it != '.' }?.take(8) ?: "Unknown"

                highScores.add(placement, Pair("${placement + 1}. $name", score))
                highScores.removeAt(10)

                val fileContent =
                    highScores.filter { it.second != "" }.joinToString("\n") { "${it.first.split('.')[1].drop(1)};${it.second}" }

                val filePath = getResource("/highscores.txt").drop(6)
                File(filePath).writeText(fileContent)

                loadHighScores()
            }
            dialog.show()
        }
    }

    override fun start(mainStage: Stage) {
        mainStage.title = "Tetris Time"
        mainStage.isResizable = false

        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        val canvas = Canvas(WIDTH, HEIGHT)
        root.children.add(canvas)

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D

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

    private fun loadHighScores() {
        highScores.clear()

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
        if (elapsedNanos > systemUpdateInterval && !paused && started) {
            map.moveCurrentTetromino(MoveDirection.DOWN)
            lastSystemUpdateTime = currentNanoTime
        }

        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH, HEIGHT)

        // draw background
        graphicsContext.fill = Color.SLATEBLUE
        graphicsContext.fillRect(0.0, 0.0, WIDTH, HEIGHT)

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
                if (!map.isMapEmpty() && !started) {
                    map = Map(::onRoundEnd, ::onScoreChange)
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
