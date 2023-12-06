package com.example.rest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MemeTemplateFragment : Fragment() {

    private lateinit var memerViewModel: MemerViewModel

    private lateinit var memeTemplateImage: ImageView
    private lateinit var prevButton : Button
    private lateinit var nextButton: Button
    private lateinit var memeTemplateIndexLabel : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.memerViewModel = ViewModelProvider(this.requireActivity())[MemerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meme_template, container, false)

        memeTemplateImage = view.findViewById(R.id.image_template)
        memeTemplateIndexLabel = view.findViewById(R.id.label_template_index)
        nextButton = view.findViewById(R.id.button_next)
        prevButton = view.findViewById(R.id.button_prev)

        nextButton.setOnClickListener{
            memerViewModel.increaseTemplateIndex()
            updateToCurrentMemeTemplate()
        }
        prevButton.setOnClickListener{
            memerViewModel.decreaseTemplateIndex()
            updateToCurrentMemeTemplate()
        }

        return view
    }

    private fun updateToCurrentMemeTemplate() {
        memeTemplateIndexLabel.text = memerViewModel.getTemplateIndex().toString()
        val meme = memerViewModel.getCurrentMemeTemplate()
        if (meme != null)
        {
            Log.d(TAG, "Meme selected: $meme")
            Picasso.get()
                .load(meme.url)
                .into(memeTemplateImage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //new values come and it gets notified
        this.memerViewModel.memeTemplateLiveData.observe(
            this.viewLifecycleOwner,
            Observer { memeTemplates ->
                Log.d(TAG, "ViewModel has noticed new meme templates: $memeTemplates")
                updateToCurrentMemeTemplate()
            }
        )
    }
}