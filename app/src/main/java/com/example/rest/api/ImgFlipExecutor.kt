package com.example.rest.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rest.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Dont do this normally
private const val callUsername = "frankzk"
private const val callPassword = "ABcd12!@"

class ImgFlipExecutor {

    private val api: ImgFlipApi
    init {
        val retrofit:Retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgflip.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.api = retrofit.create(ImgFlipApi::class.java)
    }

    fun fetchTemplates(): LiveData<List<MemeTemplateItem>> {
        val responseLiveData: MutableLiveData<List<MemeTemplateItem>> = MutableLiveData()
        val imgFlipRequest: Call<ImgFlipGetMemesResponse> = this.api.fetchTemplates()
        imgFlipRequest.enqueue(object: Callback<ImgFlipGetMemesResponse> {
            override fun onResponse(
                call: Call<ImgFlipGetMemesResponse>,
                response: Response<ImgFlipGetMemesResponse>
            ) {
                //Log.d(TAG, "Fetching..")
                val imgFlipMemesResponse: ImgFlipGetMemesResponse? = response.body()
                val imgFlipGetMemesResponseData: ImgFlipGetMemesResponseData? = imgFlipMemesResponse?.data
                var memeTemplates: List<MemeTemplateItem> = imgFlipGetMemesResponseData?.templates ?: mutableListOf()

                memeTemplates = memeTemplates.filterNot {
                    it.url.isBlank()
                }

                //Log.d(TAG, "Templates: $memeTemplates")
                responseLiveData.value = memeTemplates
            }

            override fun onFailure(call: Call<ImgFlipGetMemesResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch imgFlip meme templates")
            }

        })

        return responseLiveData
    }

    fun captionImage(templateID: String, caption: String): LiveData<ImgFlipCaptionedImageResponseData>
    {
        val responseLiveData: MutableLiveData<ImgFlipCaptionedImageResponseData> = MutableLiveData()
        val imgFlipRequest: Call<ImgFlipCaptionedImageResponse> = this.api.captionImage(
            templateID = templateID,
            username = callUsername,
            password = callPassword,
            caption1 = caption,
            caption2 = caption
        )
        imgFlipRequest.enqueue(object:Callback<ImgFlipCaptionedImageResponse>
        {
            override fun onResponse(
                call: Call<ImgFlipCaptionedImageResponse>,
                response: Response<ImgFlipCaptionedImageResponse>
            ) {
                Log.d(TAG, "Response received from ImgFlip caption_image endpoint")
                val imgFlipCaptionedImageResponse: ImgFlipCaptionedImageResponse? = response.body()
                if (imgFlipCaptionedImageResponse?.success == true)
                {
                    val imgFlipCaptionedImageResponseData: ImgFlipCaptionedImageResponseData = imgFlipCaptionedImageResponse.data
                    responseLiveData.value = imgFlipCaptionedImageResponseData
                    Log.d(TAG, "Caption image url: ${imgFlipCaptionedImageResponseData.url}")
                }
                else
                {
                    Log.d(TAG, "Request failed with error message $imgFlipCaptionedImageResponse?.errorMessage")
                }
            }

            override fun onFailure(call: Call<ImgFlipCaptionedImageResponse>, t: Throwable) {
                Log.d(TAG, "Failed to put caption")
            }
        })

        return responseLiveData
    }
}