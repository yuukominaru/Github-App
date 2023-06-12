package com.dicoding.mygithubapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.adapter.FollowAdapter
import com.dicoding.mygithubapp.databinding.FragmentFollowBinding
import com.dicoding.mygithubapp.response.FollowResponseItem
import com.dicoding.mygithubapp.viewmodel.FollowViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel>()
    private lateinit var adapter: FollowAdapter

    companion object {
        const val ARG_USERNAME = "extra_username"
        const val ARG_POSITION = "section_number"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = arguments?.getString(ARG_USERNAME)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (position == 1) {
            followViewModel.showFollowers(username.toString())
            followViewModel.follower.observe(viewLifecycleOwner) {
                showFollows(it)
            }
        } else {
            followViewModel.showFollowing(username.toString())
            followViewModel.following.observe(viewLifecycleOwner) {
                showFollows(it)
            }
        }

        followViewModel.error.observe(viewLifecycleOwner) {
            if (it)
                Toast.makeText(requireActivity(), "Error Fetching Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFollows(follow: List<FollowResponseItem>) {
        adapter = FollowAdapter(follow)
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        binding.rvFollow.setHasFixedSize(true)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}