package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.CommunityViewModel
import com.example.ui.CommunityViewModelFactory
import com.example.ui.MainLayout
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Room Database & Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val communityDao = database.communityDao()
        val repository = AppRepository(communityDao)
        
        // Instantiate ViewModel
        val factory = CommunityViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[CommunityViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainLayout(viewModel = viewModel)
            }
        }
    }
}
