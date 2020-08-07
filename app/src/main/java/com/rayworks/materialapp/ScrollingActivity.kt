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

        setupViewComponents(savedInstanceState)
    }

    private fun setupViewComponents(bundle: Bundle?) {
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationBar.setTabSelectedListener(this)

        if (bundle == null) {
            homeFragment = HomeFragment()
            fragment1 = newInstance(getString(R.string.para1), "Lesson")
            fragment2 = newInstance(getString(R.string.para2), "Me")

        } else {
            lastSelectedTabIndex = bundle.getInt("pos_last_selected", 0)

            homeFragment =
                supportFragmentManager.findFragmentByTag(tags[0]) as HomeFragment? ?: HomeFragment()
            fragment1 =
                supportFragmentManager.findFragmentByTag(tags[1]) as TextFragment? ?: newInstance(
                    getString(R.string.para1),
                    "Lesson"
                )
            fragment2 =
                supportFragmentManager.findFragmentByTag(tags[2]) as TextFragment? ?: newInstance(
                    getString(R.string.para2),
                    "Me"
                )
        }

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

        bottomNavigationBar.selectTab(lastSelectedTabIndex)
    }

    private var lastSelectedTabIndex = 0
    private val tags = arrayListOf("tagHome", "tag1", "tag2")

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt("pos_last_selected", lastSelectedTabIndex)
        }
    }

    override fun onTabReselected(position: Int) {
    }

    override fun onTabUnselected(position: Int) {
        println(">>> tab item unselected pos : $position")
    }

    override fun onTabSelected(position: Int) {
        println(">>> tab item selected pos : $position")

        val trans = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        if (lastSelectedTabIndex >= 0 && lastSelectedTabIndex < tags.size) {
            val frag = supportFragmentManager.findFragmentByTag(tags[lastSelectedTabIndex])
            if (frag != null) {
                trans.hide(frag)
            }
        }

        var fragment = supportFragmentManager.findFragmentByTag(tags[position])
        if (fragment == null) {
            fragment = when (position) {
                0 -> homeFragment
                1 -> fragment1
                else -> fragment2
            }

            trans.add(R.id.fragment_holder, fragment, tags[position]).show(fragment).commit()
        } else {
            println(">>> cached fragment : $fragment with tag ${tags[position]}")

            trans.show(fragment).commit()
        }

        lastSelectedTabIndex = position
    }
}