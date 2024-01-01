package com.example.myfirstgithubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstgithubuser.data.response.ItemsItem
import com.example.myfirstgithubuser.databinding.FragmentFollowBinding
import com.example.myfirstgithubuser.ui.adapter.UserAdapter
import com.example.myfirstgithubuser.ui.viewmodel.DetailViewModel

class FollowFragment : Fragment() {
    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_NAME = "app_name"
    }

    private var _binding : FragmentFollowBinding? = null
    private var position : Int? = null
    private val binding get() = _binding!!
    private var username : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val followViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]
        binding.rvListfollow.layoutManager = LinearLayoutManager(requireActivity())

        followViewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            showLoading(loading)
        }

        arguments?.let {
            position = it.getInt(ARG_SECTION_NUMBER)
            username = it.getString(ARG_NAME)
        }
        if (position == 1){
            binding.rvListfollow.removeAllViews()
            followViewModel.listFollower.observe(viewLifecycleOwner) { listfollow ->
                setFollowData(listfollow)
            }
        } else {
            binding.rvListfollow.removeAllViews()
            followViewModel.listFollowing.observe(viewLifecycleOwner) { listfollow ->
                setFollowData(listfollow)
            }
        }
    }

    private fun setFollowData(listfollow: List<ItemsItem>) {
        val followadapter = UserAdapter()
        followadapter.submitList(listfollow)
        binding.rvListfollow.adapter = followadapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}