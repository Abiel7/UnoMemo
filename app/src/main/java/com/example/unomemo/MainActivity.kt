package com.example.unomemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.unomemo.databinding.ActivityMainBinding

//TODO:  Converte png bildene til svg
class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var shared :SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController


        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        NavigationUI.setupWithNavController(binding.navView,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        Log.v("navigatorName",navController.currentBackStackEntry!!.destination.navigatorName)
        return NavigationUI.navigateUp(navController, drawerLayout)||super.onSupportNavigateUp()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        shared = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        //shared.edit().putString("JSON", "{}").commit()

        //adding boleans to global scope in app
        var commited = shared.edit()
            .putBoolean("splashScreenPlayed", false)
            .putBoolean("loggedIn", false)
            .putBoolean("showedLoginFirstTime", false).commit()

        Log.v(
            "MainActivity:onAttachedToWindow(); sharedPreferences()",
            ".commit() is $commited"
        )
    }
}