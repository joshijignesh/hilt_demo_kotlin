package com.kisancalculator.ui.fragments.splash

import com.kisancalculator.base.BaseViewModel
import com.kisancalculator.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val sessionManager: SessionManager) : BaseViewModel(){

    fun isLanguageSelected()  = sessionManager.getIsLanguageSelected()

}