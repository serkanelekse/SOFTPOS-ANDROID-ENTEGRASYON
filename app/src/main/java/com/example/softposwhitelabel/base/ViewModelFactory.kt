package com.example.softposwhitelabel.base

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.softposwhitelabel.ui.MainFragmentViewModel

import java.lang.IllegalArgumentException

class ViewModelFactory(): ViewModelProvider.NewInstanceFactory() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainFragmentViewModel::class.java) -> MainFragmentViewModel(application = Application()) as T

            else -> throw IllegalArgumentException("ViewModelClass Not Found")

        }
    }

}