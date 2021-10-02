package com.example.unomemo

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.example.unomemo.databinding.ActivityMainBinding





class MainActivity : AppCompatActivity() {
    lateinit var fragLayout:FragmentContainerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        fragLayout = binding.fragmentContainer

        val navController = this.findNavController(R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this,navController)

        //TODO add drawerLayout to main activity, wrap fragmentConteinerView
        //NavigationUI.setupActionBarWithNavController(this,navController,fragLayout)

        //TODO add NavigationView (matirial) bottom of layout
        //NavigationUI.setupWithNavController(binding.navView,navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =  this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }




}