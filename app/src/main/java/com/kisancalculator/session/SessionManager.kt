package com.kisancalculator.session

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context){
    private val _sessionManager = PreferenceManager.getDefaultSharedPreferences(context)

    private fun setString(key: String ,str : String){
        _sessionManager.edit().putString(key, str).apply()
    }

    private fun getString(key: String) : String = _sessionManager.getString(key, "")?:""

    private fun setBoolean(key: String ,bool : Boolean){
        _sessionManager.edit().putBoolean(key, bool).apply()
    }
    private fun getBoolean(key: String) : Boolean = _sessionManager.getBoolean(key, false)



    fun setLanguageSelector(isLanguageSelected : Boolean){
        setBoolean(IS_LANGUAGE_SELECTED, isLanguageSelected)
    }
    fun getIsLanguageSelected() =  getBoolean(IS_LANGUAGE_SELECTED)

    fun setLanguage(language: String){
        setString(PREFERRED_LANGUAGE, language)
    }
    fun getLanguage() = getString(PREFERRED_LANGUAGE)



    companion object {
        const val IS_LANGUAGE_SELECTED = "is_language_selected"
        const val PREFERRED_LANGUAGE = "preferred_language"
    }
}