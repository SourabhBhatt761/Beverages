package com.srb.beverages.ui.joke

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.srb.beverages.databinding.FragmentFoodJokeBinding

class FoodJokeFragment : Fragment() {

    private lateinit var _binding : FragmentFoodJokeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(layoutInflater)


        return binding.root
    }


}