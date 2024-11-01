package com.example.softposwhitelabel.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.softposwhitelabel.R

abstract class BaseFragment<VM: ViewModel, B: ViewBinding>: Fragment() {

    private var loadingDialog: Dialog? = null

    protected lateinit var binding: B
    protected lateinit var viewModel: VM


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        return binding.root
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    fun hideKeyboard(){
        try {
            if (activity?.currentFocus != null) {
                val inputMethodManager = context?.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager
                    .hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
            }
        } catch (e: Exception){
            //Firebase.crashlytics.recordException(e)
        }
    }

    fun displayProgressBar(display: Boolean) {
        try {
            if(display) {
                showLoading()
            } else { hideLoading()}
        } catch (e: Exception){
           // Firebase.crashlytics.recordException(e)
        }
    }

    private fun showLoading() {
        try {
            hideLoading()
            loadingDialog = context?.let { showLoadingDialog(it) }
        } catch (e: Exception){
            //Firebase.crashlytics.recordException(e)
        }
    }

    private fun hideLoading() {
        try {
            loadingDialog?.let { if(it.isShowing)it.cancel() }
        } catch (e: Exception){
            //Firebase.crashlytics.recordException(e)
        }
    }

    fun showLoadingDialog(context: Context): Dialog {
        try {
            val progressDialog = Dialog(context)

            progressDialog.let {
                it.show()
                it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                it.setContentView(R.layout.custom_loading)
                it.setCancelable(false)
                it.setCanceledOnTouchOutside(true)

                return it
            }
        } catch (e: Exception){
            //Firebase.crashlytics.recordException(e)

            return Dialog(context)
        }
    }

}