package com.example.rental_mobil.View.Customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.rental_mobil.databinding.FragmentDialogPaymentBinding

class DialogPayment : DialogFragment() {
    private lateinit var b: FragmentDialogPaymentBinding
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentDialogPaymentBinding.inflate(layoutInflater)
        v = b.root

        b.btnBayarDp.setOnClickListener {
            val dialog = HistoryPaymentFragment()

            val bundle = Bundle()
            bundle.putString("sts", "DP")
            dialog.arguments = bundle

            dialog.show(childFragmentManager, "RiwayatPembayaranFragment")
        }

        b.btnBayarLunas.setOnClickListener {
            val dialog = HistoryPaymentFragment()

            val bundle = Bundle()
            bundle.putString("sts", "Lunas")
            dialog.arguments = bundle

            dialog.show(childFragmentManager, "RiwayatPembayaranFragment")
        }

        return v
    }

    override fun dismiss() {
        super.dismiss()
    }
}