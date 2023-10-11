package com.asifddlks.starwarscharacterexplorer.api.response

import com.asifddlks.starwarscharacterexplorer.model.CharacterModel

data class CharacterResponse(
    val message: String,
    val result: Result,
) {
    data class Result(
        val properties: CharacterModel.Properties,
        val description: String,
        val uid: String,
    )
}