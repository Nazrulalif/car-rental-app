package com.example.rental_mobil.Repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rental_mobil.Model.Customer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date

class ProfilRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun profil(email: String): LiveData<Customer> {
        val resultLiveData = MutableLiveData<Customer>()

        firestore.collection("pelanggan")
            .document(email)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val data = Customer(
                        doc.get("id_pelanggan").toString(),
                        doc.get("alamat").toString(),
                        doc.get("deleted").toString(),
                        doc.get("email").toString(),
                        doc.get("foto_ktp").toString(),
                        doc.get("foto_profil").toString(),
                        doc.get("hp").toString(),
                        doc.get("nama").toString(),
                        doc.get("nik").toString(),
                        doc.get("password").toString(),
                        doc.get("ttl").toString(),
                        doc.get("updated").toString()
                    )
                    resultLiveData.value = data
                }
            }
            .addOnFailureListener { exception ->

            }

        return resultLiveData
    }

    fun edit(customer: Customer, uri: Uri?): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        val hm = HashMap<String, Any>()
        hm.set("alamat", customer.alamat)
        hm.set("hp", customer.hp)
        hm.set("nama", customer.nama)
        hm.set("nik", customer.nik)
        hm.set("updated", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        if (uri != Uri.EMPTY) {
            hm.put("foto_profil", "")
        }

        if (uri != Uri.EMPTY) {
            val fileName = "IMG" + SimpleDateFormat("yyyyMMddHHmmssSSS").format(Date()) + "new"
            val fileRef = FirebaseStorage.getInstance().reference.child(fileName + ".jpg")
            val uploadTask = uri?.let { fileRef.putFile(it) }

            uploadTask!!.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    hm.put("foto_profil", task.result.toString())

                    firestore.collection("pelanggan")
                        .document(customer.email)
                        .update(hm)
                        .addOnSuccessListener {
                            resultLiveData.value = true
                        }
                        .addOnFailureListener {
                            resultLiveData.value = false
                        }
                } else {
                    resultLiveData.value = false
                }
            }
        } else {
            firestore.collection("pelanggan")
                .document(customer.email)
                .update(hm)
                .addOnSuccessListener {
                    resultLiveData.value = true
                }
                .addOnFailureListener {
                    resultLiveData.value = false
                }
        }

        return resultLiveData
    }
}