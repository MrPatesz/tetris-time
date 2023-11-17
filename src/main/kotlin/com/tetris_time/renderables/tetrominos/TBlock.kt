package com.tetris_time.renderables.tetrominos

import com.tetris_time.renderables.Field
import com.tetris_time.enums.FieldColor
import com.tetris_time.renderables.Tetromino

class TBlock : Tetromino(
    listOf(
        Field(xIndex = 3, yIndex = 0, fieldColor = FieldColor.PURPLE),
        Field(xIndex = 4, yIndex = 0, fieldColor = FieldColor.PURPLE),
        Field(xIndex = 5, yIndex = 0, fieldColor = FieldColor.PURPLE),
        Field(xIndex = 4, yIndex = 1, fieldColor = FieldColor.PURPLE),
    )
)
