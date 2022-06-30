package com.example.foodrecipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipe.model.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase :RoomDatabase() {
    abstract fun mealDao():MealDAO

    companion object {
        @Volatile//changes in variable will be visible in all thread
        var INSTANCE: MealDatabase? = null

        @Synchronized//one thread can have only one instance of that
        fun getInstance(context: Context): MealDatabase {
            if (INSTANCE == null) {
                INSTANCE= Room.databaseBuilder(
                    context,
                    MealDatabase::class.java
                ,"meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MealDatabase
        }
    }
}