package com.example.rental_mobil.Admin

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rental_mobil.Adapter.AdapterCategory
import com.example.rental_mobil.R
import com.example.rental_mobil.databinding.ActivityCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class CategoryActivity : AppCompatActivity() {
    private lateinit var b: ActivityCategoryBinding

    val dataCategory = mutableListOf<HashMap<String,String>>()
    lateinit var categoryAdp : AdapterCategory

    var idK = ""
    var ktg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Kategori Mobil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        categoryAdp = AdapterCategory(dataCategory, this)
        b.rvKategori.layoutManager = LinearLayoutManager(this)
        b.rvKategori.adapter = categoryAdp

        b.btnAdd.setOnClickListener {
            val dialog = CategoryAddFragment()

            dialog.show(supportFragmentManager, "KategoriTambahFragment")
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
        FirebaseFirestore.getInstance().collection("kategori")
            .whereEqualTo("deleted", "")
            .get()
            .addOnSuccessListener { result ->
                dataCategory.clear()
                for (doc in result) {
                    var hm = HashMap<String, String>()
                    hm.put("id", doc.get("id_kategori").toString())
                    hm.put("kategori", doc.get("kategori").toString())
                    hm.put("created", doc.get("created").toString())

                    dataCategory.add(hm)
                }
                categoryAdp.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }

    fun deleteKategori() {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        b.progressBar.visibility = View.VISIBLE
        AlertDialog.Builder(this)
            .setTitle("Hapus Kategori!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin menghapus kategori ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                FirebaseFirestore.getInstance().collection("kategori")
                    .document(idK)
                    .delete()
                    .addOnSuccessListener {
                    Toast.makeText(this, "berhasil menghapus kategori!", Toast.LENGTH_SHORT).show()

//                    FirebaseFirestore.getInstance().collection("mobil")
//                        .whereEqualTo("kategori", ktg)
//                        .get()
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                for (document in task.result!!) {
//                                    val hm = HashMap<String, Any>()
//                                    hm.set("deleted", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
//                                    document.reference.update(hm)
//                                        .addOnSuccessListener {
//                                            Log.d(TAG, "Data mobil berhasil diperbarui")
//                                        }
//                                        .addOnFailureListener { exception ->
//                                            Log.e(TAG, "Gagal memperbarui dokumen: $exception")
//                                        }
//                                }
//                            } else {
//                                Log.e(TAG, "Gagal mendapatkan dokumen: ${task.exception}")
//                            }
//                        }

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