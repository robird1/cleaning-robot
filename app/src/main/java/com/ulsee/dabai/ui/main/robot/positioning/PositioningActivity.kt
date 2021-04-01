package com.ulsee.dabai.ui.main.robot.positioning

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ulsee.dabai.R
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.ui.main.MainActivity
import com.ulsee.dabai.ui.main.map.MapListFragment

class PositioningActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var fragment1: MapListFragment
    private lateinit var fragment2: MapPositioningFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_positioning_activity)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.positioning_step1)

        val projectID = intent.getIntExtra("project-id", 0)
        val robotID = intent.getIntExtra("robot-id", 0)
        // step1
        fragment1 = MapListFragment()
        fragment1.onMapClickListener = object: MapListFragment.OnMapClickListener {
            override fun onItemClicked(item: Map) {
                fragment2.map = item
                showStep2()
            }
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment1)
                .commitNow()
        // step2
        fragment2 = MapPositioningFragment()
        fragment2.projectID = projectID
        fragment2.robotID = robotID
    }

    fun showStep2() {
        toolbar.setTitle(R.string.positioning_step2)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment2)
                .commitNow()
    }
}