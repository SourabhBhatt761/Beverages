package com.srb.beverages.ui.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.srb.beverages.R
import com.srb.beverages.adapters.RecipesAdapter
import com.srb.beverages.databinding.FragmentRecipesBinding
import com.srb.beverages.utils.NetworkResult
import com.srb.beverages.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var _binding : FragmentRecipesBinding
    private val binding get() = _binding
    private val mAdapter by lazy { RecipesAdapter() }
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(layoutInflater)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerRv.adapter = mAdapter
        showShimmerEffect()

        mainViewModel.getRecipes(mainViewModel.applyQueries())

        mainViewModel.recipes.observe(viewLifecycleOwner,{ response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun showShimmerEffect(){
        binding.shimmerRv.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.shimmerRv.hideShimmer()
    }
}