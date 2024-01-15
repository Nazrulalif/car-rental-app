package com.example.rental_mobil.View.Admin.Car

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rental_mobil.Model.Car
import com.example.rental_mobil.R
import com.example.rental_mobil.ViewModel.CarViewModel
import com.example.rental_mobil.databinding.ActivityCarAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso

class CarAddActivity : AppCompatActivity() {
    private lateinit var b: ActivityCarAddBinding
    private lateinit var mobilVM: CarViewModel

    val RC_OK = 100
    lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCarAddBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Tambah Mobil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        uri = Uri.EMPTY
        mobilVM = ViewModelProvider(this).get(CarViewModel::class.java)

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.setType("image/*")
            startActivityForResult(intent, RC_OK)
        }

        b.btnSave.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambahkan mobil baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    if (b.insMerk.text.toString().isEmpty() || b.insStnk.text.toString().isEmpty() || b.insNama.text.toString().isEmpty() ||
                        b.insPlat.text.toString().isEmpty() || b.insTahun.text.toString().isEmpty() || b.insHarga.text.toString().isEmpty() ||
                        b.insDetail.text.toString().isEmpty()) {
                        Toast.makeText(this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    } else {
                        if (b.insCategory.selectedItem.toString().equals("-- Pilih Kategori Mobil --")) {
                            Toast.makeText(this, "Tolong pilih kategori terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        } else {
                            insert(uri)
                        }
                    }
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
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
        showKategori()
    }

    private fun showKategori() {
        FirebaseFirestore.getInstance().collection("kategori")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val dataList = mutableListOf<String>()

                dataList.add("-- Pilih Kategori Mobil --")

                for (document in querySnapshot.documents) {
                    val data = document.get("kategori").toString()
                    dataList.add(data)
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
                b.insCategory.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Tangani kegagalan mengambil data dari Firebase Firestore
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((resultCode == Activity.RESULT_OK) && (requestCode == RC_OK)) {
            if (data != null){
                uri = data.data!!
                Picasso.get().load(uri.toString()).into(b.insImage)
            }
        }
    }

    fun insert(uri: Uri) {
        val data = Car(
            "",
            b.insMerk.text.toString(),
            b.insNama.text.toString(),
            b.insHarga.text.toString(),
            b.insStnk.text.toString(),
            b.insTahun.text.toString(),
            b.insPlat.text.toString(),
            b.insCategory.selectedItem.toString(),
            "",
            b.insDetail.text.toString(),
            "",
            "",
            "",
            ""
        )
        mobilVM.insert(data, uri).observe(this, Observer { success ->
            if (success) {
                onBackPressed()
                Toast.makeText(this, "Berhasil menambah mobil baru!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menambah mobil baru!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}