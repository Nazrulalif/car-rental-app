package com.example.rental_mobil.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.rental_mobil.R
import com.example.rental_mobil.databinding.FragmentCategoryAddBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.UUID

class CategoryAddFragment : DialogFragment() {
    private lateinit var b: FragmentCategoryAddBinding
    lateinit var v: View
    lateinit var parent: CategoryActivity

    var tahun = 0
    var bulan = 0
    var hari = 0
    var jam = 0
    var menit = 0
    var detik = 0

    var jmTgl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentCategoryAddBinding.inflate(layoutInflater)
        v = b.root
        parent = activity as CategoryActivity

        val cal : Calendar = Calendar.getInstance()

        bulan = cal.get(Calendar.MONTH)+1
        hari = cal.get(Calendar.DAY_OF_MONTH)
        tahun = cal.get(Calendar.YEAR)
        jam = cal.get(Calendar.HOUR_OF_DAY)
        menit = cal.get(Calendar.MINUTE)
        detik = cal.get(Calendar.SECOND)

        jmTgl = "$tahun-$bulan-$hari $jam:$menit:$detik"

        b.close.setOnClickListener {
            dismiss()
        }

        b.btnSave.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Informasi!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menambahkan kategori baru?")
                .setPositiveButton("Ya") { dialog, _ ->
                    // Tindakan yang dilakukan saat tombol OK ditekan
                    tambah()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    // Tindakan yang dilakukan saat tombol Batal ditekan
                    dialog.dismiss()
                }
            builder.show()
        }

        return v
    }

    fun tambah() {
        val randomId = UUID.randomUUID().toString()
        val hm = HashMap<String, Any>()
        hm.set("id_kategori", randomId)
        hm.set("kategori", b.insCategory.text.toString())
        hm.set("deskripsi", b.insDescription.text.toString())
        hm.set("created", jmTgl)
        hm.set("updated", jmTgl)
        hm.set("deleted", "")

        FirebaseFirestore.getInstance().collection("kategori")
            .document(randomId).set(hm)
            .addOnSuccessListener {
                dismiss()
                parent.recreate()
                Toast.makeText(v.context, "Kategori dengan id "+b.insCategory.text.toString()+" berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
    }
}