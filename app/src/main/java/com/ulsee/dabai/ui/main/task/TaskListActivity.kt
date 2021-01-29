package com.ulsee.dabai.ui.main.task

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulsee.dabai.R
import com.ulsee.dabai.data.response.Task
import com.ulsee.dabai.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {

    private lateinit var taskListViewModel: TaskListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskListViewModel = ViewModelProvider(this, TaskListViewModelFactory())
            .get(TaskListViewModel::class.java)

        val binding: ActivityTaskListBinding = DataBindingUtil.setContentView(this, R.layout.activity_robot_list)

        // Bind layout with ViewModel
        binding.viewmodel = taskListViewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = TaskListAdapter(object: TaskListAdapter.OnItemClickListener{
            override fun onItemClicked(item: Task) {
                // todo: 建圖或執行腳本
                Toast.makeText(this@TaskListActivity, "TODO", Toast.LENGTH_LONG).show()
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        // projectID?
        val projectID = intent.getIntExtra("project-id", 0)
        taskListViewModel.getList(projectID)

        taskListViewModel.taskListResult.observe(this, Observer {
            if (it.error != null) {
                Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            }
        })
    }
}