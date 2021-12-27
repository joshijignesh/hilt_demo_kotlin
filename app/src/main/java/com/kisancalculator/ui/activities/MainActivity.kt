package com.kisancalculator.ui.activities

import androidx.activity.viewModels
import com.kisancalculator.R
import com.kisancalculator.base.BaseActivity
import com.kisancalculator.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val _mainViewModel by viewModels<MainViewModel>()

    override fun layoutId() = R.layout.activity_main

    override fun onViewCreated() {
    }
}