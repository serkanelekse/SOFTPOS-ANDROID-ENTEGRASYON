package com.example.softposwhitelabel.network.payneospos.model

import com.google.gson.annotations.SerializedName

data class PaymentCallbackRequestModel(
    @SerializedName("dealerId")
    var dealerId: String?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("mobileToken")
    var mobileToken: String?,
    @SerializedName("PaymentSessionId")
    var paymentSessionId: String?,
    @SerializedName("CallbackStatus")
    var callbackStatus: String?,
    @SerializedName("Data")
    var data: String?,
    @SerializedName("merchant")
    var merchant: String?,
    @SerializedName("merchantKey")
    var merchantKey: String?
)
