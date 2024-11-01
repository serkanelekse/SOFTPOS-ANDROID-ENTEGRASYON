package com.example.softposwhitelabel.network.payneospos.model

import com.google.gson.annotations.SerializedName

data class StartPaymentRequestModel(
    @SerializedName("dealerId")
    var dealerId: String?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("amount")
    var amount: String?,
    @SerializedName("installment")
    var installment: Int?,
    @SerializedName("mobileToken")
    var token: String?,
    @SerializedName("merchant")
    var merchant: String?,
    @SerializedName("merchantKey")
    var merchantKey: String?

)
