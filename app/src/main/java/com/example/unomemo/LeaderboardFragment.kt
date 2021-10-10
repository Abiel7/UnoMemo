package com.example.unomemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.databinding.FragmentLeaderboardBinding
import com.example.unomemo.leaderboard.LeaderboardAdapter


class LeaderboardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLeaderboardBinding>(
            inflater,
            R.layout.fragment_leaderboard,
            container,
            false
        )

        val recyclerView: RecyclerView = binding.leaderboardRecyclerview
        val leaderboardAdapter = LeaderboardAdapter()
        recyclerView.adapter = leaderboardAdapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        return binding.root
    }


}