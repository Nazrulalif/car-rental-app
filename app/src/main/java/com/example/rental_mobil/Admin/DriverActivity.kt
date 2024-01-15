package com.example.rental_mobil.Admin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rental_mobil.Adapter.AdapterDriver
import com.example.rental_mobil.R
import com.example.rental_mobil.databinding.ActivityDriverBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class DriverActivity : AppCompatActivity() {
    private lateinit var b: ActivityDriverBinding

    val dataDriver = mutableListOf<HashMap<String,String>>()
    lateinit var driverAdp : AdapterDriver

    var idS = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Daftar Sopir")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        driverAdp = AdapterDriver(dataDriver, this)
        b.rvDriver.layoutManager = LinearLayoutManager(this)
        b.rvDriver.adapter = driverAdp

        b.btnAdd.setOnClickListener {
            startActivity(Intent(this, DriverAddActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
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
        showData()
    }

    private fun showData() {
        FirebaseFirestore.getInstance().collection("sopir")
            .whereEqualTo("deleted", "")
            .get()
            .addOnSuccessListener { result ->
                dataDriver.clear()
                for (doc in result) {
                    var hm = HashMap<String, String>()
                    hm.put("id", doc.get("id_sopir").toString())
                    hm.put("nama", doc.get("nama").toString())
                    hm.put("nik", doc.get("nik").toString())
                    hm.put("status", doc.get("status").toString())
                    hm.put("ktp", doc.get("foto_ktp").toString())
                    hm.put("sim", doc.get("foto_sim").toString())

                    dataDriver.add(hm)
                }
                driverAdp.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }

    fun deleteSopir(){
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        b.progressBar.visibility = View.VISIBLE
        AlertDialog.Builder(this)
            .setTitle("Hapus Sopir!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin menghapus sopir ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                val hm = HashMap<String, Any>()
                hm.set("status", "tidak tersedia")
                hm.set("deleted", currentDateTime)
                FirebaseFirestore.getInstance().collection("sopir").document(idS).update(hm).addOnSuccessListener {
                    Toast.makeText(this, "berhasil menghapus sopir!", Toast.LENGTH_SHORT).show()
                    b.progressBar.visibility = View.GONE
                    recreate()
                }.addOnFailureListener { e ->

                }
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                b.progressBar.visibility = View.GONE
            })
            .show()
    }
}