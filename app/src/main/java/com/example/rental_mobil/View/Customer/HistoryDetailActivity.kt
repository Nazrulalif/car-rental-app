package com.example.rental_mobil.View.Customer

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rental_mobil.ImageDetailActivity
import com.example.rental_mobil.Model.Car
import com.example.rental_mobil.Model.Customer
import com.example.rental_mobil.Model.History
import com.example.rental_mobil.R
import com.example.rental_mobil.ViewModel.CarViewModel
import com.example.rental_mobil.ViewModel.CustomerViewModel
import com.example.rental_mobil.ViewModel.HistoryViewModel
import com.example.rental_mobil.ViewModel.DriverViewModel
import com.example.rental_mobil.databinding.ActivityHistoryDetailBinding
import com.squareup.picasso.Picasso
import java.io.Serializable

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityHistoryDetailBinding
    private lateinit var pVM: CustomerViewModel
    private lateinit var mVM: CarViewModel
    private lateinit var rVM: HistoryViewModel
    private lateinit var sVM: DriverViewModel

    var idR = ""
    var imgUrl = ""

    lateinit var preferences: SharedPreferences
    val PREF_NAMA = "akun"
    val EMAIL = "email"
    val DEF_EMAIL = ""
    val ID = "id_pelanggan"
    val DEF_ID = ""
    private lateinit var car: Car
    private lateinit var customer: Customer
    private lateinit var history: History

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Riwayat Detail")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = getSharedPreferences(PREF_NAMA, Context.MODE_PRIVATE)

        rVM = ViewModelProvider(this).get(HistoryViewModel::class.java)
        pVM = ViewModelProvider(this).get(CustomerViewModel::class.java)
        mVM = ViewModelProvider(this).get(CarViewModel::class.java)
        sVM = ViewModelProvider(this).get(DriverViewModel::class.java)

        b.btnConfirmationRental.setOnClickListener {
            var dialog = DialogPayment()

            dialog.show(supportFragmentManager, "DialogPembayaran")
        }

        b.btnDetailPayment.setOnClickListener {
            val intent = Intent(this, ImageDetailActivity::class.java)
            intent.putExtra("img", imgUrl)
            startActivity(intent)
        }
        b.fabPdf.setOnClickListener {
            val intent = Intent(this, PdfActivity::class.java)
            intent.putExtra("pelanggan", customer as Serializable)
            intent.putExtra("mobil", car as Serializable)
            intent.putExtra("riwayat", history as Serializable)
            startActivity(intent)
        }
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
        detailRiwayat()
    }

    fun informasi() {
        AlertDialog.Builder(this)
            .setTitle("Informasi!")
            .setIcon(R.drawable.warning)
            .setMessage("Anda telah melunasi pembayaran, Anda diperbolehkan mengambil kunci mobil sekarang!")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            .show()
    }

    fun detailRiwayat() {
        var paket : Bundle? = intent.extras
        var id = paket?.getString("id").toString()
        rVM.detail(id).observe(this, Observer { riwayat ->
            this.history = riwayat
            idR = riwayat.id_rental
            b.detailStatusRental.setText(riwayat.status_rental)
            b.detailDate.setText(riwayat.tgl_rental)
            var idP = riwayat.id_pelanggan
            var idM = riwayat.id_mobil
            var idS = riwayat.id_sopir
            b.detailDateStart.setText(riwayat.mulai_rental)
            b.detailDateEnd.setText(riwayat.selesai_rental)
            b.detailCreated.setText(riwayat.created)
            val hari = riwayat.hari_rental.toInt()
            b.detailDay.setText(riwayat.hari_rental+" Hari")
            b.detailTotal.setText("RM "+riwayat.total)
            b.detailStatusPayment.setText(riwayat.status_pembayaran)
            imgUrl = riwayat.bukti_pembayaran

            if (riwayat.status_pembayaran != "Belum Bayar") {
                b.btnConfirmationRental.visibility = View.GONE
                b.btnCancelRental.visibility = View.GONE
                b.btnConfirmationRental.visibility = View.VISIBLE
            } else {
                b.btnConfirmationRental.visibility = View.VISIBLE
                b.btnCancelRental.visibility = View.VISIBLE
                b.btnConfirmationRental.visibility = View.GONE
            }

            if (riwayat.status_pembayaran == "Lunas" && riwayat.status_rental == "Booking") {
                informasi()
                b.detailKunci.visibility = View.VISIBLE
            }

            pVM.detail(preferences.getString(EMAIL, DEF_EMAIL).toString()).observe(this, Observer { pelanggan ->
                b.detailPelanggan.setText(pelanggan.nama)
                b.detailNoHpPelanggan.setText(pelanggan.hp)
                Picasso.get().load(pelanggan.foto_profil).into(b.detailFotoPelanggan)
                this.customer = pelanggan

            })

            mVM.detail(idM).observe(this, Observer { mobil ->
                this.car = mobil

                b.detailMobil.setText(mobil.nama)
                b.detailCategory.setText(mobil.kategori)
                b.detailHarga.setText("RM "+mobil.harga)
                Picasso.get().load(mobil.foto_mobil).into(b.detailFotoMobil)

                val hrg = mobil.harga.toInt()
                val totalMobil = hari * hrg
                b.detailHargaRental.setText("RM "+totalMobil.toString())
            })

            if (idS.isNotEmpty()) {
                sVM.detail(idS).observe(this, Observer { sopir ->
                    b.detailDriver.setText(sopir.nama)
                    b.detailHpDriver.setText(sopir.hp)
                    Picasso.get().load(sopir.foto_profil).into(b.detailPhotoDriver)

                    val hrg = 50000
                    val totalSopir = hari * hrg
                    b.detailPriceDriver.setText("RM "+totalSopir.toString())
                })
            } else {
                b.cardDriver.visibility = View.GONE
                b.cdPriceDriver.visibility = View.GONE
                b.tvSopir.visibility = View.GONE
            }

            b.btnCancelRental.setOnClickListener {
                delete(idR, idM, idS)
            }
        })
    }

    fun delete(id: String, idM: String, idS: String) {
        AlertDialog.Builder(this)
            .setTitle("Batalkan!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin membatalkan rental mobil ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                rVM.delete(id, idM, idS).observe(this, Observer { result ->
                    onBackPressed()
                    Toast.makeText(this, "Berhasil membatalkan rental!", Toast.LENGTH_SHORT).show()
                })
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            .show()
    }
}