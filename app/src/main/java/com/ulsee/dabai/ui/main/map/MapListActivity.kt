package com.ulsee.dabai.ui.main.map

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

class MapListActivity : AppCompatActivity() {

    private lateinit var mapListViewModel: MapListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapListViewModel = ViewModelProvider(this, MapListViewModelFactory())
            .get(MapListViewModel::class.java)

        val binding: ActivityMapListBinding = DataBindingUtil.setContentView(this, R.layout.activity_robot_list)

        // Bind layout with ViewModel
        binding.viewmodel = mapListViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = MapListAdapter(object: MapListAdapter.OnItemClickListener{

            override fun onItemClicked(item: com.ulsee.dabai.data.response.Map) {
                // todo: 建圖或執行腳本
                Toast.makeText(this@MapListActivity, "TODO", Toast.LENGTH_LONG).show()
            }

            override fun onUpload(item: Map) {
                // ignore
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        // projectID?
        val projectID = intent.getIntExtra("project-id", 0)
        mapListViewModel.getProjectMapList(projectID)

        mapListViewModel.projectMapListResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            }
        })
    }
}