package com.example.foodrecipe.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodrecipe.R
import com.example.foodrecipe.activities.CategoryMealActivity
import com.example.foodrecipe.activities.MainActivity
import com.example.foodrecipe.activities.MealActivity
import com.example.foodrecipe.adapters.CategoriesAdapter
import com.example.foodrecipe.databinding.FragmentCategoriesBinding
import com.example.foodrecipe.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.example.foodrecipe.viewModel.HomeViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    private lateinit var viewModel:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Or categoriesAdapter=CategoriesAdapter()
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeCategories()

        onCategoryItemClick()
    }

    private fun onCategoryItemClick() {
        categoriesAdapter.onItemClick={category->
            val intent = Intent(activity,CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    /*val intent = Intent(activity, CategoryMealActivity::class.java)
    intent.putExtra(CATEGORY_NAME, category.strCategory)
    startActivity(intent)*/

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter=categoriesAdapter
        }

    }
}