package com.example.rental_mobil.Admin

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rental_mobil.Adapter.AdapterCustomer
import com.example.rental_mobil.R
import com.example.rental_mobil.databinding.ActivityCustomerAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class CustomerActivity : AppCompatActivity() {
    private lateinit var b: ActivityCustomerAdminBinding

    val dataCustomer = mutableListOf<HashMap<String,String>>()
    lateinit var customerAdp : AdapterCustomer

    lateinit var db : FirebaseFirestore

    var em = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCustomerAdminBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Daftar Pelanggan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        customerAdp = AdapterCustomer(dataCustomer, this)
        b.rvCustomer.layoutManager = LinearLayoutManager(this)
        b.rvCustomer.adapter = customerAdp

        db = FirebaseFirestore.getInstance()
        showData("")
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

//        val randomId = UUID.randomUUID().toString()
//        Toast.makeText(this, randomId, Toast.LENGTH_SHORT).show()
    }

    fun showData(nm: String) {
        val query = db.collection("pelanggan")
            .whereEqualTo("deleted", "")

        if (nm.equals("")) {
            query.whereGreaterThanOrEqualTo("nama", nm)
                .whereLessThanOrEqualTo("nama", nm + "\uf8ff")
        }

        query.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    dataCustomer.clear()
                    if (result != null) {
                        for (doc in result.documents) {
                            var hm = HashMap<String, String>()
                            hm.put("id", doc.get("id_pelanggan").toString())
                            hm.put("email", doc.get("email").toString())
                            hm.put("nama", doc.get("nama").toString())
                            hm.put("nik", doc.get("nik").toString())
                            hm.put("ktp", doc.get("foto_ktp").toString())

                            dataCustomer.add(hm)
                        }
                        customerAdp.notifyDataSetChanged()
                    } else {
                        // Tangani jika hasil permintaan Firestore null
                    }
                }
            }
    }

    fun deletePelanggan(){
        b.progressBar.visibility = View.VISIBLE
        AlertDialog.Builder(this)
            .setTitle("Hapus Pelanggan!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin menghapus pelanggan ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                val hm = HashMap<String, Any>()
                hm.set("deleted", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
                db.collection("pelanggan").document(em).update(hm).addOnSuccessListener {
                    Toast.makeText(this, "berhasil menghapus pelanggan!", Toast.LENGTH_SHORT).show()
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