package com.example.rest.api

import com.google.gson.annotations.SerializedName

class ImgFlipCaptionedImageResponse {

    @SerializedName("Success")
    var success = false

    @SerializedName("Data")
    lateinit var data: ImgFlipCaptionedImageResponseData

    @SerializedName("error_message")
    lateinit var errorMessage: String
}