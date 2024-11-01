package com.example.starwars.data.mapper

import com.example.starwars.data.local.PlayerInfoEntity
import com.example.starwars.data.remote.response.PlayerItemDto
import com.example.starwars.domain.model.Player

fun PlayerItemDto.toPlayerInfoEntity() : PlayerInfoEntity {
    return PlayerInfoEntity(
        id = id,
        name = name,
        image = icon.replace("http", "https"), // image is not fetched when url starts with http:/
        score = 0
    )
}

fun PlayerInfoEntity.toPlayer(): Player {
    return Player(
        id = id,
        name = name,
        icon = image,
        score = score.toString()
    )
}