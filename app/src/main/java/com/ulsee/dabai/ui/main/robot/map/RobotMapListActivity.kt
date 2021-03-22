package com.ulsee.dabai.ui.main.robot.map

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulsee.dabai.R
import com.ulsee.dabai.data.request.UploadMapRequest
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.databinding.ActivityRobotlocalMaplistBinding
import com.ulsee.dabai.ui.main.map.MapListAdapter
import com.ulsee.dabai.ui.main.map.MapListViewModel
import com.ulsee.dabai.ui.main.map.MapListViewModelFactory

class RobotMapListActivity : AppCompatActivity() {

    private lateinit var cloudMapListViewModel: MapListViewModel
    private lateinit var binding: ActivityRobotlocalMaplistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val projectID = intent?.getIntExtra("project-id", 0) ?: 0
        val robotID = intent?.getIntExtra("robot-id", 0) ?: 0

        cloudMapListViewModel = ViewModelProvider(this, MapListViewModelFactory())
                .get(MapListViewModel::class.java)

        // val binding: ActivityRobotlocalMaplistBinding = DataBindingUtil.setContentView(this, R.layout.activity_robotlocal_maplist)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_robotlocal_maplist)

        // Bind layout with ViewModel
        binding.viewmodel = cloudMapListViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = MapListAdapter(object: MapListAdapter.OnItemClickListener{
            override fun onItemClicked(item: Map) {
                // ignore
            }

            override fun onUpload(item: Map) {
                Toast.makeText(this@RobotMapListActivity, "on upload: ${item.map_name}", Toast.LENGTH_SHORT).show()
                val payload = UploadMapRequest(robot_id = robotID)
                cloudMapListViewModel.upload(projectID, item.map_id, payload)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        cloudMapListViewModel.projectMapListResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            } else {
                reloadMapUploadable()
            }
        })
        cloudMapListViewModel.getProjectMapList(projectID)


        cloudMapListViewModel.robotMapListResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            } else {
                reloadMapUploadable()
            }
        })
        cloudMapListViewModel.getRobotMapList(projectID, robotID)

        cloudMapListViewModel.uploadMapResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            } else {
                // 重新取得專案地圖
                cloudMapListViewModel.getProjectMapList(projectID)
            }
        })
    }

    fun reloadMapUploadable() {
        if (cloudMapListViewModel.projectMapListResult.value?.success == null) return
        if (cloudMapListViewModel.robotMapListResult.value?.success == null) return

        cloudMapListViewModel.robotMapListResult.value?.success?.let {
            for(map in it) {
                map.uploadable = cloudMapListViewModel.projectMapListResult.value?.success?.find { o -> o.map_id == map.map_id } == null
            }
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}