package com.example.foodrecipe.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodrecipe.R
import com.example.foodrecipe.adapters.CategoryMealsAdapter
import com.example.foodrecipe.databinding.ActivityCategoryMealBinding
import com.example.foodrecipe.fragments.HomeFragment
import com.example.foodrecipe.viewModel.CategoryMealsViewModel

class CategoryMealActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryMealBinding
    lateinit var categoryMealsViewModel:CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList->
            categoryMealsAdapter.setMealsList(mealsList)
            binding.tvCategoryCount.text="${intent.getStringExtra(HomeFragment.CATEGORY_NAME)}: ${mealsList.size}"
        })

        categoryMealsClick()


    }

    private fun categoryMealsClick() {
        categoryMealsAdapter.OnItemClick={meals->
            var intent= Intent(this,MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meals.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meals.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meals.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter=categoryMealsAdapter
        }
    }
}