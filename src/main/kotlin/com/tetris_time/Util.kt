package com.tetris_time

fun getResource(filename: String): String {
    return Game::class.java.getResource(filename).toString()
}
