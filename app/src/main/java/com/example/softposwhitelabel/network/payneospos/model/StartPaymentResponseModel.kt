package com.example.softposwhitelabel.network.payneospos.model

import com.google.gson.annotations.SerializedName

data class StartPaymentResponseModel(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("PaymentSessionId")
    var paymentSessionId: String?
)
