package com.rayworks.materialapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.rayworks.materialapp.TextFragment.Companion.newInstance

class ScrollingActivity : AppCompatActivity(), BottomNavigationBar.OnTabSelectedListener {
    private lateinit var bottomNavigationBar: BottomNavigationBar

    private lateinit var fragment1: TextFragment
    private lateinit var fragment2: TextFragment
    private lateinit var homeFragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setupViewComponents()
    }

    private fun setupViewComponents() {
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationBar.setTabSelectedListener(this)

        homeFragment = HomeFragment()
        fragment1 = newInstance(getString(R.string.para1), "Lesson")
        fragment2 = newInstance(getString(R.string.para2), "Me")

        initNavigationItems()
    }

    private fun initNavigationItems() {
        bottomNavigationBar.clearAll()

        val holoBlueDark = android.R.color.holo_blue_dark
        bottomNavigationBar.apply {
            addItem(
                BottomNavigationItem(
                    R.drawable.ic_launcher_background,
                    "Home"
                ).setActiveColorResource(holoBlueDark)
            )
            addItem(
                BottomNavigationItem(
                    R.drawable.ic_launcher_foreground,
                    "Lesson"
                ).setActiveColorResource(holoBlueDark)
            )
            addItem(
                BottomNavigationItem(
                    R.drawable.ic_launcher_background,
                    "Me"
                ).setActiveColorResource(holoBlueDark)
            )

            initialise()
        }

        bottomNavigationBar.selectTab(0)

    }

    override fun onTabReselected(position: Int) {
    }

    override fun onTabUnselected(position: Int) {
    }

    val tags = arrayListOf("tagHome", "tag1", "tag2")
    override fun onTabSelected(position: Int) {
        println(">>> tab item selected pos : $position")

        val trans = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        val fragment = supportFragmentManager.findFragmentByTag(tags[position])
        if (fragment == null) {
            trans.replace(
                R.id.fragment_holder,
                when (position) {
                    0 -> homeFragment
                    1 -> fragment1
                    else -> fragment2
                },
                tags[position]
            ).commit()
        } else {

            for ((index, tag) in tags.withIndex()) {
                if (index == position) {
                    trans.show(fragment)
                } else {
                    val frag = supportFragmentManager.findFragmentByTag(tag)
                    frag?.let {
                        trans.hide(it)
                    }
                }
            }
            trans.commit()
        }
    }
}