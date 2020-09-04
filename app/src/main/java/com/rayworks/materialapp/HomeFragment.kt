package com.rayworks.materialapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
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

    private var maxScrollDist = 0

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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        activity!!.window.statusBarColor =
            if (hidden) resources.getColor(R.color.colorPrimary)
            else Color.TRANSPARENT // fit status bar
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

        maxScrollDist = resources.getDimensionPixelSize(R.dimen.vertical_scrollable_distance)

        val appBar = root.findViewById<AppBarLayout>(R.id.app_bar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            println(">>> verticalOffset : $verticalOffset")

            val maxTextDist = maxScrollDist / 5

            var toAlpha: Float = 1.0f - abs(verticalOffset) * 1.0f / maxScrollDist
            cardLeft.alpha = toAlpha
            cardRight.alpha = toAlpha

            toAlpha = 1.0f - abs(verticalOffset) * 1.0f / maxTextDist
            txtProgress.alpha = toAlpha
            txtMore.alpha = toAlpha
            if (toAlpha.equals(1.0f)) {
                activity!!.window.statusBarColor = Color.TRANSPARENT
            }
        })

        val bkgView = root.findViewById<View>(R.id.bkg_view)
        val header = root.findViewById<RelativeLayout>(R.id.profile_header)
        val hey = root.findViewById<TextView>(R.id.hey)

        val nameTextView = root.findViewById<TextView>(R.id.name)
        nameTextView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (nameTextView.width > 0 && toolbarLayout.height > 0) {

                    nameTextView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val start = nameTextView.x.toInt()
                    val top = nameTextView.y.toInt()

                    println(">> start, top : $start, $top")

                    toolbarLayout.expandedTitleMarginTop = top - nameTextView.height
                    toolbarLayout.expandedTitleMarginStart = start

                    // for toolbar and cardItem clipped paddings
                    maxScrollDist = toolbarLayout.height - dpToPx(context!!, 112).toInt()

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