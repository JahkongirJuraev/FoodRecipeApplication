package com.example.foodrecipe.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.activities.MainActivity
import com.example.foodrecipe.activities.MealActivity
import com.example.foodrecipe.adapters.MealAdapter
import com.example.foodrecipe.databinding.FragmentFavouritiesBinding
import com.example.foodrecipe.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class favouritiesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritiesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesMealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorities()
        onFavoriteMealClick()

        val itemTOuchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesMealAdapter.differ.currentList[position])

                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(favoritesMealAdapter.differ.currentList[position])
                    }
                )
            }

        }
    }

    private fun onFavoriteMealClick() {
        favoritesMealAdapter.onItemClick = { meals ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meals.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meals.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meals.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        favoritesMealAdapter = MealAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesMealAdapter
        }
    }

    private fun observeFavorities() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer { meals ->
            favoritesMealAdapter.differ.submitList(meals)
        })
    }


}