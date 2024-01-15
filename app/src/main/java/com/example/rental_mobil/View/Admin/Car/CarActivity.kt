package com.example.rental_mobil.View.Admin.Car

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rental_mobil.Adapter.AdapterCar
import com.example.rental_mobil.R
import com.example.rental_mobil.ViewModel.CarViewModel
import com.example.rental_mobil.databinding.ActivityCarBinding

class CarActivity : AppCompatActivity() {
    private lateinit var b : ActivityCarBinding
    private lateinit var mobilVM: CarViewModel

    lateinit var mobilAdp : AdapterCar

    var idM = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCarBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setTitle("Daftar Mobil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mobilVM = ViewModelProvider(this).get(CarViewModel::class.java)

        mobilAdp = AdapterCar(ArrayList(), this)
        b.rvMobil.layoutManager = GridLayoutManager(this, 2)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        b.rvMobil.addItemDecoration(itemDecoration)
        b.rvMobil.adapter = mobilAdp

        b.btnAdd.setOnClickListener {
            startActivity(Intent(this, CarAddActivity::class.java))
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
        loadData()
    }

    fun loadData() {
        mobilVM.loadData().observe(this, Observer { mobilList ->
            mobilAdp.setData(mobilList)
        })
    }

    fun deleteMobil() {
        b.progressBar.visibility = View.VISIBLE
        AlertDialog.Builder(this)
            .setTitle("Hapus Daftar!")
            .setIcon(R.drawable.warning)
            .setMessage("Apakah Anda ingin menghapus daftar mobil ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                mobilVM.delete(idM).observe(this, Observer { success ->
                    Toast.makeText(this, "Berhasil menghapus data!", Toast.LENGTH_SHORT).show()
                    loadData()
                    b.progressBar.visibility = View.GONE
                })
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                b.progressBar.visibility = View.GONE
            })
            .show()
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