package com.tetris_time.renderables.tetrominos

import com.tetris_time.renderables.Field
import com.tetris_time.enums.FieldColor
import com.tetris_time.renderables.Tetromino

class OBlock : Tetromino(
    listOf(
        Field(xIndex = 3, yIndex = 0, fieldColor = FieldColor.YELLOW),
        Field(xIndex = 4, yIndex = 0, fieldColor = FieldColor.YELLOW),
        Field(xIndex = 3, yIndex = 1, fieldColor = FieldColor.YELLOW),
        Field(xIndex = 4, yIndex = 1, fieldColor = FieldColor.YELLOW),
    )
)