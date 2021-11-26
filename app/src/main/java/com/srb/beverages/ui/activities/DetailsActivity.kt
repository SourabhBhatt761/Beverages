package com.srb.beverages.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.srb.beverages.R
import com.srb.beverages.adapters.PagerAdapter
import com.srb.beverages.data.database.entities.FavoritesEntity
import com.srb.beverages.databinding.ActivityDetailsBinding
import com.srb.beverages.ui.fragments.viewPagerFragments.IngredientsFragment
import com.srb.beverages.ui.fragments.viewPagerFragments.InstructionsFragment
import com.srb.beverages.ui.fragments.viewPagerFragments.OverviewFragment
import com.srb.beverages.utils.Constants.RECIPE_RESULT_KEY
import com.srb.beverages.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)           //'<-' this arrow will come up to go back to home

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
            resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

            val pagerAdapter = PagerAdapter(
                resultBundle,
                fragments,
                this
            )
//            binding.viewPager2.isUserInputEnabled = false           //to prevent scroll feature.
            binding.viewPager2.apply {
                adapter = pagerAdapter
            }

            //to set the selected tab fragments name
            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text = titles[position]
            }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.result.recipeId == args.result?.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                        break
                    }
                }
            } catch (e: Exception) {
                Timber.d(e.message.toString())
            }
        })
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity =
            args.result?.let {                          //make sures that result isn't null
                FavoritesEntity(
                    0,
                    it
                )
            }
        if (favoritesEntity != null) {
            mainViewModel.insertFavoriteRecipe(favoritesEntity)
        }
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity =
            args.result?.let {
                FavoritesEntity(
                    savedRecipeId,
                    it
                )
            }
        if (favoritesEntity != null) {
            mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        }
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}