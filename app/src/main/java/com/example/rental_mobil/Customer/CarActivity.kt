package com.example.rental_mobil.Customer

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rental_mobil.Adapter.AdapterCustomerCar
import com.example.rental_mobil.R
import com.example.rental_mobil.databinding.ActivityCarBinding
import com.google.firebase.firestore.FirebaseFirestore

class CarActivity : AppCompatActivity() {
    private lateinit var b : ActivityCarBinding

    val dataMobil = mutableListOf<HashMap<String,String>>()
    lateinit var mobilAdp : AdapterCustomerCar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCarBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Daftar Mobil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mobilAdp = AdapterCustomerCar(dataMobil, this)
        b.rvMobil.layoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        b.rvMobil.addItemDecoration(itemDecoration)
        b.rvMobil.adapter = mobilAdp

        b.btnAdd.visibility = View.GONE
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
        FirebaseFirestore.getInstance().collection("mobil")
            .whereEqualTo("deleted", "")
            .get()
            .addOnSuccessListener { result ->
                dataMobil.clear()
                for (doc in result) {
                    var hm = HashMap<String, String>()
                    hm.put("id", doc.get("id_mobil").toString())
                    hm.put("kategori", doc.get("kategori").toString())
                    hm.put("nama", doc.get("nama").toString())
                    hm.put("foto_mobil", doc.get("foto_mobil").toString())
                    hm.put("status", doc.get("status").toString())

                    dataMobil.add(hm)
                }
                mobilAdp.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Mendapatkan posisi item
            val column = position % spanCount // Mendapatkan kolom saat ini

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 2) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 2) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}