package com.ulsee.dabai.ui.robot_local

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.ButterKnife
import com.ulsee.dabai.R
import com.ulsee.dabai.data.RobotWIFIRepository
import com.ulsee.dabai.data.response.MapInfo
import io.github.controlwear.virtual.joystick.android.JoystickView

private val TAG = ExploreActivity::class.java.simpleName

class ExploreActivity: AppCompatActivity() {
//    @BindView(R.id.imageView)
    lateinit var imageView: ImageView
    var positionTV: TextView? = null
    var mapContainer: RelativeLayout? = null
    var robotIV: ImageView? = null

    lateinit var progressBar: ProgressBar

    var mMapSize : Size? = null

    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var viewModel: ExploreViewModel
    private lateinit var robotRepository: RobotWIFIRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)
        supportActionBar!!.title = "建圖模式 ULSee"
        ButterKnife.bind(this)
        positionTV = findViewById(R.id.textView_position)
        robotIV = findViewById(R.id.imageView_robot)
        mapContainer = findViewById(R.id.relativeLayout)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)
        initRobotRepository()
        initViewModel()
        initExitCreateMapObserver()
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.isIndeterminate = true

        initJoyStick()

        showDynamicMap()
    }

    private fun initExitCreateMapObserver() {
        viewModel.exitCreateMapResult.observe(this, Observer {
            progressBar.visibility = View.INVISIBLE
            finish()
        })
    }

    private fun initRobotRepository() {
        robotRepository = RobotWIFIRepository()

        val ctx = this
        robotRepository.setEventListener(object : RobotWIFIRepository.EventListener {
            override fun onPositionUpdated(pose: Array<Double>) {
                updatePosition(pose[0], pose[1])
            }

            override fun onMapUpdated(data: MapInfo) {
                updateMapInfo(data)
            }

            override fun onConnected() {
                runOnUiThread { Toast.makeText(ctx, "connected", Toast.LENGTH_SHORT).show() }
            }

            override fun onConnectFailed(exception: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        ctx,
                        "connect failed: " + exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                finish()
            }

            override fun onError(exception: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        ctx,
                        "error: " + exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.explore_activity_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editor_action_more -> {
//                binding.progressBar.isVisible = true
                progressBar.visibility = View.VISIBLE

                val mapName = intent.getStringExtra("map_name")
                val mapFloor = intent.getStringExtra("map_floor")
                Log.d(TAG, "mapName: $mapName mapFloor: $mapFloor")
                if (!mapName.isNullOrEmpty() && !mapFloor.isNullOrBlank()) {
                    viewModel.exitCreateMap(mapName!!, mapFloor!!)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDynamicMap() {
        viewModel.loadDynamicMap(this, imageView)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, ExploreViewModelFactory())
            .get(ExploreViewModel::class.java)
    }

    private fun initJoyStick() {
        val isStop = booleanArrayOf(false)
        val movement = doubleArrayOf(0.0)
        val rotation = doubleArrayOf(0.0)
        (findViewById<View>(R.id.joystick) as JoystickView).setOnMoveListener { intAngle: Int, intStrength: Int ->
//            Log.d(TAG, "[Enter] onMoveListener() intAngle: "+ intAngle+ "intStrength: "+intStrength);
            //joystickOnMove!!.text = "angle: $intAngle strength: $intStrength"

            val angle = intAngle.toDouble()
            val strength = intStrength.toDouble()
            rotation[0] = 0.0
            movement[0] = strength / 100.0
            isStop[0] = intAngle == 0 && intStrength == 0
            //            isStop[0] = intStrength == 0;
            if (isStop[0]) {
                stopMove()
                return@setOnMoveListener
            }
            // calculate movement forward & backward
            /* 0~ 1*/if (angle > 0 && angle <= 90) movement[0] = movement[0] * angle / 90.0
            /* 1~ 0*/if (angle > 90 && angle <= 180) movement[0] =
            movement[0] * (1.0 - (angle - 90.0) / 90.0)
            /* 0~-1*/if (angle > 180 && angle <= 270) movement[0] =
            movement[0] * (-(angle - 180.0) / 90.0)
            /*-1~ 0*/if (angle > 270 && angle < 360) movement[0] =
            movement[0] * (-1 + (angle - 270.0) / 90.0)
            // calculate rotate
            /*-1~ 0*/if (angle > 0 && angle <= 90) rotation[0] = -(90.0 - angle) / 90.0
            /* 0~ 1*/if (angle > 90 && angle <= 180) rotation[0] = (angle - 90.0) / 90.0
            /* 1~ 0*/if (angle > 180 && angle <= 270) rotation[0] = 1 - (angle - 180.0) / 90.0
            /* 0~-1*/if (angle > 270 && angle < 360) rotation[0] = -(angle - 270.0) / 90
            // 靠近上下時不轉彎，靠近左右時不前後移動
            if (angle > 70 && angle < 110 || angle > 250 && angle < 290) rotation[0] = 0.0
            if (angle > 340 || angle < 20 || angle > 160 && angle < 200) movement[0] = 0.0
            // 下方，靠近正左正右時，轉彎方向不變
//            if (angle > 340 || angle > 180 && angle < 200) rotation[0] = -rotation[0];

            if (isStop[0]) return@setOnMoveListener

            if (movement[0] != 0.0) {
                robotRepository.move(movement[0])
            }
            if (rotation[0] != 0.0) {
                robotRepository.rotate(rotation[0] * 50)
            }
        }
    }

    private fun stopMove() {
        robotRepository.move(0.0)
    }

    fun updatePosition(x: Double, y: Double) {
        val dp = resources.displayMetrics.density.toDouble()

        if (viewModel != null) viewModel.updateRobotPosition(mapContainer!!, robotIV!!, positionTV!!,x, y, dp)
    }

    fun updateMapInfo(data: MapInfo) {
        if (viewModel != null) viewModel.updateMapInfo(data)
    }
}