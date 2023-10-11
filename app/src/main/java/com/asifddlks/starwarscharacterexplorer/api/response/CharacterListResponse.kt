package com.asifddlks.starwarscharacterexplorer.api.response

import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import com.google.gson.annotations.SerializedName

data class CharacterListResponse(
    val results: List<CharacterModel>,
    val next: String?,
    @SerializedName("total_pages")
    val totalPages: Int,
)