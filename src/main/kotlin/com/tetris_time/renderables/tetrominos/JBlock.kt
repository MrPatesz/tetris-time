package com.tetris_time.renderables.tetrominos

import com.tetris_time.renderables.Field
import com.tetris_time.enums.FieldColor
import com.tetris_time.renderables.Tetromino

class JBlock : Tetromino(
    listOf(
        Field(xIndex = 4, yIndex = 0, fieldColor = FieldColor.BLUE),
        Field(xIndex = 5, yIndex = 0, fieldColor = FieldColor.BLUE),
        Field(xIndex = 6, yIndex = 0, fieldColor = FieldColor.BLUE),
        Field(xIndex = 6, yIndex = 1, fieldColor = FieldColor.BLUE),
    )
)