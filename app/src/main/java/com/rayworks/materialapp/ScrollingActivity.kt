package com.rayworks.materialapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlin.math.abs

class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setupViewComponents()
    }

    private fun setupViewComponents() {
        setSupportActionBar(findViewById(R.id.toolbar))
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
        toolbarLayout.title = "Rita Wang"//title

        val cardLeft = findViewById<CardView>(R.id.lesson_card_left)
        val cardRight = findViewById<CardView>(R.id.lesson_card_right)

        val txtProgress = findViewById<TextView>(R.id.text_progress)
        val txtMore = findViewById<TextView>(R.id.text_more)

        val maxScrollDist = resources.getDimensionPixelSize(R.dimen.vertical_scrollable_distance)
        val maxTextDist = maxScrollDist / 5

        val appBar = findViewById<AppBarLayout>(R.id.app_bar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            println(">>> verticalOffset : $verticalOffset")

            var toAlpha: Float = 1.0f - abs(verticalOffset) * 1.0f / maxScrollDist
            cardLeft.alpha = toAlpha
            cardRight.alpha = toAlpha

            toAlpha = 1.0f - abs(verticalOffset) * 1.0f / maxTextDist
            txtProgress.alpha = toAlpha
            txtMore.alpha = toAlpha
        })

        val nameTextView = findViewById<TextView>(R.id.name)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item)
    }
}