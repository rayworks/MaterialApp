package com.rayworks.materialapp

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.abs

class HomeFragment : Fragment() {
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        initView()
    }

    private fun initView() {

        val toolbarLayout: CollapsingToolbarLayout = root.findViewById(R.id.toolbar_layout)
        toolbarLayout.title = "Rita Wang"//title

        val toolbar = root.findViewById<Toolbar>(R.id.toolbar)
        getCompatActivity().setSupportActionBar(toolbar)

        // set the background explicitly instead of 'app:contentScrim' to reduce the view flicking
        getCompatActivity().supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.colorPrimary
                )
            )
        )

        val cardLeft = root.findViewById<CardView>(R.id.lesson_card_left)
        val cardRight = root.findViewById<CardView>(R.id.lesson_card_right)

        val txtProgress = root.findViewById<TextView>(R.id.text_progress)
        val txtMore = root.findViewById<TextView>(R.id.text_more)

        val maxScrollDist = resources.getDimensionPixelSize(R.dimen.vertical_scrollable_distance)
        val maxTextDist = maxScrollDist / 5

        val appBar = root.findViewById<AppBarLayout>(R.id.app_bar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            println(">>> verticalOffset : $verticalOffset")

            var toAlpha: Float = 1.0f - abs(verticalOffset) * 1.0f / maxScrollDist
            cardLeft.alpha = toAlpha
            cardRight.alpha = toAlpha

            toAlpha = 1.0f - abs(verticalOffset) * 1.0f / maxTextDist
            txtProgress.alpha = toAlpha
            txtMore.alpha = toAlpha
        })

        val nameTextView = root.findViewById<TextView>(R.id.name)
        nameTextView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (nameTextView.width > 0) {

                    nameTextView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val start = nameTextView.x.toInt()
                    val top = nameTextView.y.toInt()

                    println(">> start, top : $start, $top")

                    toolbarLayout.expandedTitleMarginTop = top
                    toolbarLayout.expandedTitleMarginStart = start

                    nameTextView.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun getCompatActivity() = (activity as AppCompatActivity)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_scrolling, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId.equals(R.id.action_scan)) {
            Toast.makeText(getCompatActivity(), "Scan it !", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }
}