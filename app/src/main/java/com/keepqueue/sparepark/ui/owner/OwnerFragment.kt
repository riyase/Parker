package com.keepqueue.sparepark.ui.owner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepqueue.sparepark.data.MyPreferences
import com.keepqueue.sparepark.data.model.Space
import com.keepqueue.sparepark.data.response.Result
import com.keepqueue.sparepark.databinding.FragmentOwnerBinding
import com.keepqueue.sparepark.ui.home.BookSpaceActivity
import com.keepqueue.sparepark.ui.owner.add.AddSpaceActivity
import com.keepqueue.sparepark.ui.owner.spacedetails.SpaceDetailsActivity

class OwnerFragment : Fragment() {

    private var _binding: FragmentOwnerBinding? = null
    private lateinit var viewModel: OwnerViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val itemClickListener = object: OwnerAdapter.Listener {

        override fun onSpaceClick(space: Space) {
            val intent = Intent(activity, SpaceDetailsActivity::class.java)
            intent.putExtra(BookSpaceActivity.SPACE, space)
            startActivity(intent)
        }
    }
    private val homeAdapter = OwnerAdapter(itemClickListener)



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(OwnerViewModel::class.java)
        _binding = FragmentOwnerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvSpaces.adapter = homeAdapter
        binding.rvSpaces.layoutManager = LinearLayoutManager(activity)
        binding.fabAddSpace.setOnClickListener {
            val intent = Intent(requireContext(), AddSpaceActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSpaces(MyPreferences.getUserId(requireActivity()))
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSpaces(MyPreferences.getUserId(requireActivity()))
        viewModel.getSpacesResult.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    homeAdapter.setSpaceList(result.data)
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.swipeRefresh.isRefreshing = false
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.swipeRefresh.isRefreshing = false
                    result.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}