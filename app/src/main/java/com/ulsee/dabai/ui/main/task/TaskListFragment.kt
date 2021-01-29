package com.ulsee.dabai.ui.main.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulsee.dabai.R
import com.ulsee.dabai.data.response.Task
import com.ulsee.dabai.databinding.FragmentTaskListBinding
import com.ulsee.dabai.ui.main.MainActivity

class TaskListFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  ViewModelProvider(this, TaskListViewModelFactory())
            .get(TaskListViewModel::class.java)

        val projectID = (activity as? MainActivity)?.projectID ?: 0

        val binding: FragmentTaskListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_list, container, false)

        // Bind layout with ViewModel
        binding.viewmodel = viewModel

        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = TaskListAdapter(object: TaskListAdapter.OnItemClickListener{
            override fun onItemClicked(item: Task) {
                // todo: 建圖或執行腳本
                Toast.makeText(context, "TODO", Toast.LENGTH_LONG).show()
            }
        })
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        viewModel.getList(projectID)

        viewModel.taskListResult.observe(requireActivity(), {
            if (it.error != null) {
                Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }
}