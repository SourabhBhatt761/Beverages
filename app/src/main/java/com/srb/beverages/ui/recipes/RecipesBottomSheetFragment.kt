package com.srb.beverages.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.srb.beverages.R


class RecipesBottomSheetFragment : BottomSheetDialogFragment() {


//    private lateinit var _binding : RecipesBottom
//    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//            val applyBtn = requireActivity().findViewById<MaterialButton>( R.id.apply_btn)
//        applyBtn.setOnClickListener {
//            Toast.makeText(requireContext(), "btn clicked", Toast.LENGTH_SHORT).show()
//        }



       return inflater.inflate(R.layout.reciepes_bottom_sheet,container,false)

    }

}