package com.example.softposwhitelabel.ui.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.softposwhitelabel.ui.receipt.adapter.ReceiptAdapter
import com.example.softposwhitelabel.databinding.FragmentReceiptBinding
import com.provisionpay.android.deeplinksdk.model.Transaction

class ReceiptFragment(var transaction: Transaction?) : DialogFragment() {
    private lateinit var adapter: ReceiptAdapter
    private lateinit var rrn_value : TextView
    private lateinit var receiptBinding: FragmentReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,  android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        receiptBinding = FragmentReceiptBinding.inflate(inflater,container,false)

        receiptBinding.okBtn.setOnClickListener {
           findNavController().popBackStack()
        }

        return receiptBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ReceiptAdapter()

        super.onViewCreated(view, savedInstanceState)

    }

    /*private fun createPaymentDoneReceipt(paymentResponse: Transaction): ArrayList<ReceiptRowItem>{
        val list = ArrayList<ReceiptRowItem>()

        paymentResponse.Receipt?.Detail?.forEach { item ->
            val isApproved = item.Key == "Status" && paymentResponse.Receipt!!.Approved

            receiptBinding.rrnValueText.text = " RRN = "+paymentResponse.Rrn
        }
        return list
    }*/
}