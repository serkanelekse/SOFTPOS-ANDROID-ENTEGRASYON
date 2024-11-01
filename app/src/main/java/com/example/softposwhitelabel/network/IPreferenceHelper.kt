package com.example.softposwhitelabel.network

interface IPreferenceHelper {

    fun setToken(userToken: String)
    fun getToken(): String

    fun setUserId(userId: String)
    fun getUserId(): String

    fun setDealerId(dealerId: String)
    fun getDealerId(): String


}