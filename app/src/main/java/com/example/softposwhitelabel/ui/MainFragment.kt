package com.example.softposwhitelabel.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.softposwhitelabel.network.payneospos.model.PaymentCallbackRequestModel
import com.example.softposwhitelabel.network.payneospos.model.StartPaymentRequestModel
import com.example.softposwhitelabel.R
import com.example.softposwhitelabel.base.BaseFragment
import com.example.softposwhitelabel.databinding.FragmentMainBinding
import com.example.softposwhitelabel.network.Constants
import com.example.softposwhitelabel.network.IPreferenceHelper
import com.example.softposwhitelabel.network.PreferenceManager
import com.google.gson.Gson
import com.provisionpay.android.deeplinksdk.SoftposDeeplinkSdk
import com.provisionpay.android.deeplinksdk.SoftposDeeplinkSdkListener
import com.provisionpay.android.deeplinksdk.broadcastReceiver.BroadcastReceiverListener
import com.provisionpay.android.deeplinksdk.model.InitializeConfig
import com.provisionpay.android.deeplinksdk.model.IntentDataFlow
import com.provisionpay.android.deeplinksdk.model.PaymentFailedResult
import com.provisionpay.android.deeplinksdk.model.SoftposErrorType
import com.provisionpay.android.deeplinksdk.model.Transaction


class MainFragment : BaseFragment<MainFragmentViewModel, FragmentMainBinding>() {

    var paymentSession: String = ""
    private val preferenceHelper: IPreferenceHelper by lazy { PreferenceManager(requireContext()) }

    override fun getViewModel(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*isAppInstalled = appInstalledOrNot("com.provisionpay.softpos.elekse")*/
        try {
            setupUI()
            handleClick()
            observeLoading()
            observeResponses()
            observeNetworkErrors()
        } catch (ex: Exception) {
            //Ex
        }

        val privateKey = Constants.softPosPrivateKey
        val activity = requireActivity()
        val softPosUrl = Constants.softPosUrl
        SoftposDeeplinkSdk.initialize(
            InitializeConfig(
                privateKey = privateKey,
                activity = activity,
                deeplinkUrl = softPosUrl
            )
        )
        SoftposDeeplinkSdk.setDebugMode(true)

        SoftposDeeplinkSdk.subscribe(object : SoftposDeeplinkSdkListener {
            override fun onCancel() {
                Log.d("SOFTPOS", "onCancel)")

                //val declineFragment = DeclineFragment()
                //declineFragment.show(fragmentManager!!, "Decline")

                viewModel.endPayment(createRequestModel("1", ""), findNavController())
            }

            override fun onError(e: Throwable) {
                Log.d("SOFTPOS", "onError")

                //val declineFragment = DeclineFragment()
                //declineFragment.show(fragmentManager!!, "Decline")

                val gson = Gson()
                val transactionGson = gson.toJson(e)
                viewModel.endPayment(createRequestModel("2", transactionGson), findNavController())
            }

            override fun onIntentData(dataFlow: IntentDataFlow, data: String?) {
                Log.d("SOFTPOS", "onIntentData")

                //val gson = Gson()
                //val transactionGson = gson.toJson(data)
            }

            override fun onOfflineDecline(paymentFailedResult: PaymentFailedResult?) {
                Log.d("SOFTPOS", "onOfflineDecline")

                //val declineFragment = DeclineFragment()
                //declineFragment.show(fragmentManager!!, "Decline")

                val gson = Gson()
                val transactionGson = gson.toJson(paymentFailedResult)

                viewModel.endPayment(createRequestModel("4", transactionGson), findNavController())
            }

            override fun onPaymentDone(transaction: Transaction, isApproved: Boolean) {
                Log.d("SOFTPOS", "onPaymentDone")

                //val receiptFragment = ReceiptFragment(transaction)
                //receiptFragment.show(fragmentManager!!, "Receipt")

                val gson = Gson()
                val transactionGson = gson.toJson(transaction)

                viewModel.endPayment(createRequestModel("5", transactionGson), findNavController())

            }

            override fun onSoftposError(errorType: SoftposErrorType, description: String?) {
                Log.d("SOFTPOS", "onSoftposError")

                //val declineFragment = DeclineFragment()
                //declineFragment.show(fragmentManager!!, "Decline")

                val gson = Gson()
                val transactionGson = gson.toJson(description)


                viewModel.endPayment(createRequestModel("6", transactionGson), findNavController())
            }

            override fun onTimeOut() {
                Log.d("SOFTPOS", "onTimeOut")

                //val declineFragment = DeclineFragment()
                //declineFragment.show(fragmentManager!!, "Decline")

                viewModel.endPayment(createRequestModel("7", ""), findNavController())
            }
        })

        SoftposDeeplinkSdk.registerBroadcastReceiver("com.provisionpay.softpos.esnekpos",
            object : BroadcastReceiverListener {
                override fun onSoftposBroadcastReceived(
                    eventType: Int,
                    eventTypeMessage: String,
                    paymentSessionToken: String
                ) {
                    Log.d("SOFTPOS", "onSoftposBroadcastReceived")
                }
            }
        )

    }

    private fun createRequestModel(status: String, data: String): PaymentCallbackRequestModel {
        val paymentCallbackRequestModel = PaymentCallbackRequestModel(
            dealerId = preferenceHelper.getDealerId(),
            userId = preferenceHelper.getUserId(),
            mobileToken = preferenceHelper.getToken(),
            paymentSessionId = paymentSession,
            callbackStatus = status,
            data = data,
            merchant = "",
            merchantKey = ""
        )

        return paymentCallbackRequestModel
    }

    // region UI Process
    private fun setupUI() {

        viewModel.paymentCallbackResponseModel.observe(viewLifecycleOwner, Observer { response ->
            response?.let {

            }
        })
    }

    private fun handleClick() {
        binding.btnStartPayment.setOnClickListener {
            if (isPackageInstalled(
                    packageName = "com.provisionpay.softpos.esnekpos",
                    packageManager = requireContext().packageManager
                )
            ) {
                val startPaymentRequestModel = StartPaymentRequestModel(
                    dealerId = preferenceHelper.getDealerId(),
                    userId = preferenceHelper.getUserId(),
                    amount = "1",
                    installment = 1,
                    token = preferenceHelper.getToken().toString(),
                    merchant = "",
                    merchantKey = ""
                )
                viewModel.startPayment(startPaymentRequestModel)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Lütfen esnekpos uygulamasını yükleyiniz",
                    Toast.LENGTH_LONG
                ).show()
                //go Play store
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.provisionpay.softpos.esnekpos")
                        )
                    )
                }
                catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.provisionpay.softpos.esnekpos")
                        )
                    )
                }
            }

        }
    }

    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (loading) {
                    displayProgressBar(true)
                } else {
                    displayProgressBar(false)
                }
            }
        }
    }

    // endregion ObservableData
    private fun observeResponses() {
        var count = 0;
        viewModel.startPaymentResponseModel.observe(viewLifecycleOwner, Observer { responses ->
            responses?.let {
                if (responses.status == true) {
                    paymentSession = responses.paymentSessionId.toString()
                    SoftposDeeplinkSdk.startPayment(
                        responses.paymentSessionId.toString(),
                        Constants.softPosUrl
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        responses.message ?: context?.resources?.getString(R.string.softpos_error1),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun observeNetworkErrors() {
        viewModel.networkError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Log.e("networkError", error.message.toString())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        SoftposDeeplinkSdk.unregisterBroadcastReceiver()
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        try {
            packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

}