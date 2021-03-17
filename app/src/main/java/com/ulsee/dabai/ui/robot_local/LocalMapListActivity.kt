package com.ulsee.dabai.ui.robot_local

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulsee.dabai.R
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.databinding.ActivityMapListBinding
import com.ulsee.dabai.databinding.ActivityRobotlocalMaplistBinding
import com.ulsee.dabai.ui.main.map.MapListAdapter

class LocalMapListActivity : AppCompatActivity() {

    private lateinit var mapListViewModel: LocalMapListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapListViewModel = ViewModelProvider(this, LocalMapListViewModelFactory())
                .get(LocalMapListViewModel::class.java)

        val binding: ActivityRobotlocalMaplistBinding = DataBindingUtil.setContentView(this, R.layout.activity_robotlocal_maplist)

        // Bind layout with ViewModel
        binding.viewmodel = mapListViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = MapListAdapter(object: MapListAdapter.OnItemClickListener{
            override fun onItemClicked(item: Map) {
                // ignore
            }

            override fun onUpload(item: Map) {
                // todo: upload
                Toast.makeText(this@LocalMapListActivity, "on upload: ${item.map_name}", Toast.LENGTH_SHORT).show()
//                val payload = PositioningRequest(map_id = 88044496, pose = PositioningRequestPose(0, 0, 0))
//                viewModel.positioning(projectID, item.robot_id, payload)
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        mapListViewModel.mapListResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            }
        })
        mapListViewModel.getList()
    }
}