package com.kisancalculator.ui.fragments.splash

import androidx.fragment.app.viewModels
import com.kisancalculator.R
import com.kisancalculator.base.BaseFragment
import com.kisancalculator.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(){

    private val _splashViewModel by viewModels<SplashViewModel>()

    override fun layoutId() = R.layout.fragment_splash

    override fun onViewCreated() {
        _splashViewModel.isLanguageSelected()
    }

    override fun viewModelObservers() {
    }
}