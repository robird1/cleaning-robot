package com.ulsee.dabai.ui.robot

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulsee.dabai.R
import com.ulsee.dabai.data.response.Robot
import com.ulsee.dabai.databinding.ActivityRobotListBinding

class RobotListActivity : AppCompatActivity() {

    private lateinit var robotListViewModel: RobotListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        robotListViewModel = ViewModelProvider(this, RobotListViewModelFactory())
                .get(RobotListViewModel::class.java)

        val binding: ActivityRobotListBinding = DataBindingUtil.setContentView(this, R.layout.activity_robot_list)

        // Bind layout with ViewModel
        binding.viewmodel = robotListViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = RobotListAdapter(object: RobotListAdapter.OnItemClickListener{
            override fun onItemClicked(item: Robot) {
                // todo: 建圖或執行腳本
                Toast.makeText(this@RobotListActivity, "TODO", Toast.LENGTH_LONG).show()
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        // projectID?
        val projectID = intent.getIntExtra("project-id", 0)
        robotListViewModel.getList(projectID)

        robotListViewModel.robotListResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            }
        })
    }
}