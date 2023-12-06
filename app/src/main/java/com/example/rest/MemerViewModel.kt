package com.example.rest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rest.api.ImgFlipCaptionedImageResponseData
import com.example.rest.api.ImgFlipExecutor
import com.example.rest.api.MemeTemplateItem

class MemerViewModel : ViewModel() {
    private var templateIndex = 0

    val memeTemplateLiveData: LiveData<List<MemeTemplateItem>> = ImgFlipExecutor().fetchTemplates()
    var captionedMemeLiveData: LiveData<ImgFlipCaptionedImageResponseData> = MutableLiveData<ImgFlipCaptionedImageResponseData>()

    fun setTemplateIndex(index : Int){
        this.templateIndex = index
    }
    fun getTemplateIndex() : Int {
        return this.templateIndex
    }

    fun increaseTemplateIndex() : Int
    {
        templateIndex += 1
        if (templateIndex >= (memeTemplateLiveData.value?.size ?: 0))
        {
            templateIndex = 0
        }
        return templateIndex
    }
    fun decreaseTemplateIndex() : Int
    {
        templateIndex -= 1
        if (templateIndex <0 && ((memeTemplateLiveData.value?.size ?: 0) > 0))
        {
            templateIndex = memeTemplateLiveData.value!!.size
        }
        return templateIndex
    }

    fun getCurrentMemeTemplate(): MemeTemplateItem? {
        if (memeTemplateLiveData.value != null &&
            (templateIndex>= 0 && templateIndex <= memeTemplateLiveData.value!!.size))
        {
            return memeTemplateLiveData.value!![this.templateIndex]
        }
        else
        {
            return null
        }
    }

    fun captionMeme(templateID: String, caption: String): LiveData<ImgFlipCaptionedImageResponseData>
    {
        Log.d(TAG, "Received request to caption meme $templateID with $caption")
        this.captionedMemeLiveData = ImgFlipExecutor().captionImage(templateID, caption)

        return captionedMemeLiveData
    }
}