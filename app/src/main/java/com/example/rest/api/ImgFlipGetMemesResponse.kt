package com.example.rest.api

import com.google.gson.annotations.SerializedName

//contains key "data" and contains all the memes, list of memes
class ImgFlipGetMemesResponse {
    @SerializedName("data")
    lateinit var data: ImgFlipGetMemesResponseData

}