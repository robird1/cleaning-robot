package com.ulsee.dabai.ui.main.map

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
import com.ulsee.dabai.data.response.Map
import com.ulsee.dabai.databinding.FragmentMapListBinding

class MapListFragment : Fragment() {

    private lateinit var viewModel: MapListViewModel

    interface OnMapClickListener{
        fun onItemClicked(item: Map)
    }
    public var onMapClickListener : OnMapClickListener? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  ViewModelProvider(this, MapListViewModelFactory())
            .get(MapListViewModel::class.java)
        val binding: FragmentMapListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_list, container, false)

        // Bind layout with ViewModel
        binding.viewmodel = viewModel
        // LiveData needs the lifecycle owner
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = MapListAdapter(object: MapListAdapter.OnItemClickListener{
            override fun onItemClicked(item: Map) {
                onMapClickListener?.onItemClicked(item)
            }

            override fun onUpload(item: Map) {
                // ignore
            }
        })
        // recylerview
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)

        // projectID?
        val projectID = activity?.intent?.getIntExtra("project-id", 0) ?: 0
        viewModel.getProjectMapList(projectID)

        viewModel.projectMapListResult.observe(requireActivity(),  {
            if (it.error != null) {
                Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }
}