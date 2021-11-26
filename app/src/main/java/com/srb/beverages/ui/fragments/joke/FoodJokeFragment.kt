package com.srb.beverages.ui.fragments.joke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.srb.beverages.R
import com.srb.beverages.databinding.FragmentFoodJokeBinding
import com.srb.beverages.utils.Constants
import com.srb.beverages.utils.NetworkResult
import com.srb.beverages.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private lateinit var _binding : FragmentFoodJokeBinding
    private val binding get() = _binding

    private val mainViewModel by viewModels<MainViewModel>()
    private var foodJoke = "No Food Joke"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(Constants.API_KEY)
        mainViewModel.foodJoke.observe(viewLifecycleOwner, { response ->
            Timber.e("inside response")
            when(response){
                is NetworkResult.Success -> {
                    binding.foodJokeTextView.text = response.data?.text

                    if(response.data != null){
                        foodJoke = response.data.text.toString()
                    }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                }
            }
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share_food_joke_menu){
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner, {database->
                if(!database.isNullOrEmpty()){

                    binding.foodJokeTextView.text = database.first().foodJoke.text
                    foodJoke = database.first().foodJoke.text.toString()
                }
            })
        }
    }


}