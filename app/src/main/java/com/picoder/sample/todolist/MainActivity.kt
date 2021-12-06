package com.picoder.sample.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

// because fragment of this activity is entry point, so parent activity must be entry point as well
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // will change action bar as fragment show
        setupActionBarWithNavController(findNavigationController())
    }

    // handle click navigation up
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavigationController()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun findNavigationController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        return navHostFragment.navController
    }
}