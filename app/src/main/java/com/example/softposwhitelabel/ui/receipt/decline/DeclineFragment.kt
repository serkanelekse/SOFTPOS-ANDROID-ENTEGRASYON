package com.example.softposwhitelabel.ui.receipt.decline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.softposwhitelabel.ui.receipt.adapter.ReceiptAdapter
import com.example.softposwhitelabel.databinding.FragmentDeclineBinding


class DeclineFragment() : DialogFragment() {
    private lateinit var adapter: ReceiptAdapter
    private lateinit var rrn_value : TextView
    private lateinit var declineBinding: FragmentDeclineBinding

    private val args: DeclineFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,  android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        declineBinding = FragmentDeclineBinding.inflate(inflater,container,false)

        declineBinding.okBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return declineBinding.root

    }

    override fun onResume() {
        super.onResume()
        declineBinding.textView.text = args.messageText
    }
}