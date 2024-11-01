package com.example.softposwhitelabel.network.payneospos

import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackRequestModel
import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackResponseModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentRequestModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentResponseModel
import com.example.softposwhitelabel.network.Constants
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PaymentApiInstance {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    private val paymentApiBuild =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(PosApi::class.java)

    fun startPayment(
        startPaymentRequestModel: StartPaymentRequestModel
    ): Single<StartPaymentResponseModel> {

        return paymentApiBuild.startPayment(
            startPaymentRequestModel
        )
    }

    fun requestPosServiceCallback(paymentCallbackRequestModel: PaymentCallbackRequestModel): Single<PaymentCallbackResponseModel> {
        return paymentApiBuild.requestPosServiceCallback(paymentCallbackRequestModel)
    }


}