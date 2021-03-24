package com.ulsee.dabai.ui.robot_local

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ulsee.dabai.R
import com.ulsee.dabai.ui.login.afterTextChanged

class CreateMapActivity: AppCompatActivity() {
    private lateinit var viewModel: CreateMapViewModel
    private lateinit var mapName: EditText
    private lateinit var mapFloor: EditText
    private lateinit var createBtn: Button
    private lateinit var loading: ProgressBar

    val REQUEST_CODE_CREATE_MAP_EXPLORE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_map)
        mapName = findViewById<EditText>(R.id.mapname)
        mapFloor = findViewById<EditText>(R.id.floor)
        createBtn = findViewById<Button>(R.id.create_button)
        loading = findViewById<ProgressBar>(R.id.loading)

        initViewModel()
        initBtnListener()

        viewModel.createMapFormState.observe(this, Observer {
            val createMapFormState = it ?: return@Observer

            // disable login button unless both username / password is valid
            createBtn.isEnabled = it.isDataValid

            if (createMapFormState.mapNameError != null) {
                mapName.error = getString(createMapFormState.mapNameError)
            }
            if (createMapFormState.mapFloorError != null) {
                mapFloor.error = getString(createMapFormState.mapFloorError)
            }
        })

        viewModel.createMapResult.observe(this, Observer {
            val createMapResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (createMapResult.error != null) {
                showCreateFailed(createMapResult.error)
            }
            if (createMapResult.success != null) {
                val intent = Intent(this, ExploreActivity::class.java)
                intent.putExtra("map_name", mapName.text.toString())
                intent.putExtra("map_floor", mapFloor.text.toString())
                startActivityForResult(intent, REQUEST_CODE_CREATE_MAP_EXPLORE)
            }
        })

        mapName.afterTextChanged {
            viewModel.loginDataChanged(
                mapName.text.toString(),
                mapFloor.text.toString()
            )
        }

        mapFloor.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    mapName.text.toString(),
                    mapFloor.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.createMap(
                            mapName.text.toString(),
                            mapFloor.text.toString()
                        )
                }
                false
            }

        }

    }

    private fun initBtnListener() {
        val button = findViewById<Button>(R.id.create_button)
        button.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.createMap(mapName.text.toString(), mapFloor.text.toString())
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, CreateMapViewModelFactory())
            .get(CreateMapViewModel::class.java)
    }

    private fun showCreateFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CREATE_MAP_EXPLORE) {
            if (resultCode == RESULT_OK) {
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

