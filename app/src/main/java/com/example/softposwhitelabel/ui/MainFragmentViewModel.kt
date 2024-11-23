package com.example.softposwhitelabel.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.softposwhitelabel.network.payneospos.PaymentApiInstance
import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackRequestModel
import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackResponseModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentRequestModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentResponseModel
import com.example.softposwhitelabel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainFragmentViewModel(application: Application): BaseViewModel(application) {

    val loading = MutableLiveData<Boolean>()
    val networkError = MutableLiveData<Throwable>()
    private val disposable = CompositeDisposable()

    // region Pay
    val startPaymentResponseModel = MutableLiveData<StartPaymentResponseModel?>()
    val paymentCallbackResponseModel = MutableLiveData<PaymentCallbackResponseModel?>()
    private val paymentApiInstance = PaymentApiInstance()

    fun startPayment(startPaymentRequestModel: StartPaymentRequestModel){
        try {
            Log.d("startPayRequest", startPaymentRequestModel.toString())
            loading.value = true
            disposable.add(
                paymentApiInstance.startPayment(startPaymentRequestModel)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<StartPaymentResponseModel>(){
                        override fun onSuccess(response: StartPaymentResponseModel) {
                            Log.d("startPayResponse", response.toString())
                            loading.value = false
                            startPaymentResponseModel.value = response
                            startPaymentResponseModel.value = null
                        }
                        override fun onError(e: Throwable) {
                            loading.value = false
                            networkError.value = e
                            Log.d("startPayError","${e.message.toString()} ")
                        }
                    })
            )
        } catch (e: Exception){
            loading.value = false
            networkError.value = e
            Log.d("startPayError","${e.message.toString()} ")
        }
    }

    fun endPayment(paymentCallbackRequestModel: PaymentCallbackRequestModel, navController: NavController){
        try {

            loading.value = true
            disposable.add(
                paymentApiInstance.requestPosServiceCallback(paymentCallbackRequestModel)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<PaymentCallbackResponseModel>(){
                        override fun onSuccess(response: PaymentCallbackResponseModel) {
                            Log.d("startPayResponse", response.toString())
                            loading.value = true
                            paymentCallbackResponseModel.value = response
                            /*Handler(Looper.getMainLooper()).postDelayed({
                                if (response.status == true) {
                                    navController.navigate(HomeFragmentDirections.actionHomeFragmentToReceiptFragment())
                                } else {
                                    navController.navigate(HomeFragmentDirections.actionHomeFragmentToDeclineFragment(response.message ?: "İşlem Başarısız"))
                                }
                                paymentCallbackResponseModel.value = null
                            }, 1000)*/
                            paymentCallbackResponseModel.value = null
                        }

                        override fun onError(e: Throwable) {
                            loading.value = true
                            networkError.value = e
                            Log.d("startPayError","${e.message.toString()} ")
                        }
                    })
            )
        } catch (e: Exception){
            loading.value = true
            networkError.value = e
            Log.d("startPayError","${e.message.toString()} ")
        }
    }

}