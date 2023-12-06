package com.example.rest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class RenderedMemeFragment : Fragment() {

    private lateinit var memerViewModel: MemerViewModel

    private lateinit var captionInput : EditText
    private lateinit var captionButton : Button
    private lateinit var captionedMemeImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.memerViewModel = ViewModelProvider(this.requireActivity())[MemerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rendered_meme, container, false)

        this.captionInput = view.findViewById(R.id.edit_text_caption_input)
        this.captionButton = view.findViewById(R.id.button_caption)
        this.captionedMemeImage = view.findViewById(R.id.image_rendered_meme)

        captionButton.setOnClickListener{
            val text = captionInput.text.toString()
            val memeTemplate = memerViewModel.getCurrentMemeTemplate()
            if (text == null){
                Log.d(TAG, "Caption text is null")
            }
            else if (memeTemplate == null) {
                Log.d(TAG, "There is no selected template")
            }
            else {
                Log.d(TAG, "Requesting caption $text to be added to $memeTemplate")
                this.memerViewModel.captionMeme(templateID = memeTemplate.id, caption = text)
                    .observe(
                        this.viewLifecycleOwner,
                        Observer{ captionedMemeResponseData ->
                            Log.d(TAG, "Fragment has noticed a meme has been created: $captionedMemeResponseData")
                            Picasso.get()
                                .load(captionedMemeResponseData.url)
                                .into(this.captionedMemeImage)
                        }
                    )
            }
        }


        return view
    }
}