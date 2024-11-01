package com.example.softposwhitelabel.network.payneospos.model

import com.google.gson.annotations.SerializedName

data class PaymentCallbackResponseModel (
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("data")
    var data: String?
)