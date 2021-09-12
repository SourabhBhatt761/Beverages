package com.srb.beverages.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.srb.beverages.R
import com.srb.beverages.databinding.FragmentReciepesBottomSheetBinding
import com.srb.beverages.utils.Constants.DEFAULT_DIET_TYPE
import com.srb.beverages.utils.Constants.DEFAULT_MEAL_TYPE
import com.srb.beverages.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception
import java.util.*

@AndroidEntryPoint
class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var _binding : FragmentReciepesBottomSheetBinding
    private val binding get() = _binding

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentReciepesBottomSheetBinding.inflate(layoutInflater)

        //reading the data from viewModel when bottom sheet opens
        readDataFromViewModel()


        //setting up the data in bottom sheet
        setUpDataInBottomSheet()


        //hitting applyBtn
        applyButtonClickListener()

       return binding.root

    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)
            } catch (e: Exception) {
                Timber.d(e.message.toString())
            }
        }
    }

    private fun readDataFromViewModel(){
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        })
    }

    private fun setUpDataInBottomSheet(){
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }
    }

    private fun applyButtonClickListener() {
        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
        }

//        val action =
//                RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheet()
//            findNavController().navigate(action)
    }




}