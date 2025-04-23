package com.example.quizapp

import android.content.Context
import android.content.res.Configuration
import java.util.Locale
import androidx.core.content.edit

object LocalizationManager {
    private const val LANGUAGE_KEY = "selected_language"

    fun setLocale(context: Context) {
        val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val languageCode = sharedPrefs.getString(LANGUAGE_KEY, Locale.getDefault().language) ?: "ru"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun setNewLocale(context: Context, languageCode: String) {
        val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit() { putString(LANGUAGE_KEY, languageCode) }
        setLocale(context)
    }
}