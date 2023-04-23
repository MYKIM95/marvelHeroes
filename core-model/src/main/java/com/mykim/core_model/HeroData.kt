package com.mykim.core_model

data class HeroResponseData (
    val code: Int = 0,
    val data: CharacterData
)

data class CharacterData(
    val offset: Int = 0,
    val limit: Int = 0,
    val total: Int = 0,
    val count: Int = 0,
    val results: List<HeroData> = listOf()
)

data class HeroData(
    val id: Int = 0,
    val name: String = "",
    val description : String = "",
    val thumbnail: Thumbnail = Thumbnail(),
    val isFavorite: Boolean = false
)

data class Thumbnail(
    val path : String = "",
    val extension: String = ""
){
    fun toImageUrl() = "$path.$extension"
}