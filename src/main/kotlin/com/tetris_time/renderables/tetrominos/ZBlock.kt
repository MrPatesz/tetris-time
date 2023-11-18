package com.tetris_time.renderables.tetrominos

import com.tetris_time.renderables.Field
import com.tetris_time.enums.FieldColor
import com.tetris_time.renderables.Tetromino

class ZBlock : Tetromino(
    listOf(
        Field(xIndex = 4, yIndex = 0, fieldColor = FieldColor.RED),
        Field(xIndex = 5, yIndex = 1, fieldColor = FieldColor.RED),
        Field(xIndex = 6, yIndex = 1, fieldColor = FieldColor.RED),
        Field(xIndex = 5, yIndex = 0, fieldColor = FieldColor.RED),
    )
)