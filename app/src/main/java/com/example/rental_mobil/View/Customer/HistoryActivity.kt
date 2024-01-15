package com.example.rental_mobil.View.Customer

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rental_mobil.Adapter.AdapterHistory
import com.example.rental_mobil.R
import com.example.rental_mobil.ViewModel.HistoryViewModel
import com.example.rental_mobil.databinding.ActivityHistoryCustomerBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var b: ActivityHistoryCustomerBinding
    private lateinit var rVM : HistoryViewModel
    lateinit var adapter: AdapterHistory

    lateinit var preferences: SharedPreferences
    val PREF_NAMA = "akun"
    val EMAIL = "email"
    val DEF_EMAIL = ""
    val ID = "id_pelanggan"
    val DEF_ID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHistoryCustomerBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Riwayat Rental Mobil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = getSharedPreferences(PREF_NAMA, Context.MODE_PRIVATE)
        rVM = ViewModelProvider(this).get(HistoryViewModel::class.java)

        adapter = AdapterHistory(ArrayList(), this)
        b.recycleView.layoutManager = LinearLayoutManager(this)
        b.recycleView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        rVM.loadRiwayat(preferences.getString(ID, DEF_ID).toString()).observe(this, Observer { riwayatList ->
            adapter.setData(riwayatList)
        })
    }

    fun delete(id: String, idM: String, idS: String) {
        AlertDialog.Builder(this)
            .setTitle("Batalkan!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin membatalkan rental mobil ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                rVM.delete(id, idM, idS).observe(this, Observer { result ->
                    loadData()
                    Toast.makeText(this, "Berhasil membatalkan rental!", Toast.LENGTH_SHORT).show()
                })
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            .show()
    }
}