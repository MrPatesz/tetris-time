package com.tetris_time

import com.tetris_time.renderables.Tetromino
import com.tetris_time.renderables.tetrominos.*
import kotlin.random.Random

fun getResource(filename: String): String {
    return Game::class.java.getResource(filename).toString()
}

fun getRandomTetromino(): Tetromino {
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
