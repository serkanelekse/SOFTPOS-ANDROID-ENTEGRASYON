package com.example.softposwhitelabel.network.payneospos

import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackRequestModel
import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackResponseModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentRequestModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentResponseModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface PosApi {
    @POST("SoftPosStartPaymentsdk")
    fun startPayment(@Body startPaymentRequestModel: StartPaymentRequestModel): Single<StartPaymentResponseModel>

    @POST("SoftPosPaymentCallback")
    fun requestPosServiceCallback(@Body paymentCallbackRequestModel: PaymentCallbackRequestModel): Single<PaymentCallbackResponseModel>
}