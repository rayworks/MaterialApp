package com.rayworks.materialapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_text.*
import kotlin.random.Random

internal const val KEY_MESSAGE = "message"
internal const val KEY_TITLE = "txt_title"

class TextFragment : Fragment() {
    companion object {
        fun newInstance(message: String, title: String): TextFragment {
            val textFragment = TextFragment()

            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            args.putString(KEY_TITLE, title)

            textFragment.arguments = args
            return textFragment
        }

    }

    private var msg: String? = null
    private var title: String? = null
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        msg = arguments?.getString(KEY_MESSAGE, "")
        title = arguments?.getString(KEY_TITLE, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_text, container, false)
        (root.findViewById<TextView>(R.id.tf_textview)).text = msg ?: ""
        return root
    }

    private fun getCompatActivity() = (activity as AppCompatActivity)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCompatActivity().setSupportActionBar(root.findViewById(R.id.toolbar_text))

        getCompatActivity().supportActionBar?.title = title

        progress_circular.apply {
            setRounded(true)
            setProgressWidth(dpToPx(activity!!, 4))
            animateProgress(Random.Default.nextInt(0, 101).toFloat())
        }
    }
}