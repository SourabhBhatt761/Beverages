package com.srb.gourmet.ui.fragments.viewPagerFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.srb.gourmet.data.network.models.Result
import com.srb.gourmet.databinding.FragmentInstructionsBinding
import com.srb.gourmet.utils.Constants

class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)


        binding.instructionsWebView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {

                //use this to show progress bar
                binding.apply {
                    progressBar.visibility = View.GONE
                    instructionsWebView.visibility = View.VISIBLE
                }
            }
        }
        val websiteUrl: String = myBundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(websiteUrl)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}