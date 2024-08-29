package com.example.scoreboardbrlk.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.scoreboardbrlk.R
import com.example.scoreboardbrlk.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var _binding: FragmentMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)

        _binding.buttonScore.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_menuFragment_to_scoreFragment))
        return _binding.root
    }
}