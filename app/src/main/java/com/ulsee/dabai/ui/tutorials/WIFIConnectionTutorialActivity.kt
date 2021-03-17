package com.ulsee.dabai.ui.tutorials

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ulsee.dabai.R
import com.ulsee.dabai.ui.robot_local.CreateMapActivity

class WIFIConnectionTutorialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_wifi_connection)

        initBtnListener()
    }

    private fun initBtnListener() {
        val button = findViewById<Button>(R.id.button_submit)
        button.setOnClickListener {
            val intent = Intent(this, CreateMapActivity::class.java)
            startActivity(intent)
        }
    }
}

