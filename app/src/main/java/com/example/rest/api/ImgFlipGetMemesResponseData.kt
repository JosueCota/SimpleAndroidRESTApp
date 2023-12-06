package com.example.rest.api

import com.google.gson.annotations.SerializedName


//serialization to turn string into a field
//list of memes
class ImgFlipGetMemesResponseData {

    @SerializedName("memes")
    lateinit var templates: List<MemeTemplateItem>

}