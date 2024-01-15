package com.example.rental_mobil.Repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rental_mobil.Model.Car
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class CarRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun insert(car: Car, uri: Uri?): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        val randomId = UUID.randomUUID().toString()
        val hm = HashMap<String,String>()
        hm.put("id_mobil", randomId)
        hm.set("merk", car.merk)
        hm.set("nama", car.nama)
        hm.set("harga", car.harga)
        hm.set("stnk", car.stnk)
        hm.set("tahun", car.tahun)
        hm.set("plat", car.plat)
        hm.set("keterangan", car.keterangan)
        hm.set("kategori", car.kategori)
        hm.set("status", "tersedia")
        hm.set("deleted", "")
        hm.set("created", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        hm.set("updated", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        if (uri != Uri.EMPTY) {
            hm.put("foto_mobil", "")
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
                    hm.put("foto_mobil", task.result.toString())

                    firestore.collection("mobil")
                        .document(randomId)
                        .set(hm)
                        .addOnSuccessListener {
                            result.value = true
                        }
                        .addOnFailureListener {
                            result.value = false
                        }
                } else {
                    result.value = false
                }
            }
        } else {
            firestore.collection("mobil")
                .document(randomId)
                .set(hm)
                .addOnSuccessListener {
                    result.value = true
                }
                .addOnFailureListener {
                    result.value = false
                }
        }

        return result
    }

    fun loadData(): LiveData<List<Car>> {
        val resultLiveData = MutableLiveData<List<Car>>()

        firestore.collection("mobil")
            .whereEqualTo("deleted", "")
            .get()
            .addOnSuccessListener { result ->
                val dataCar = mutableListOf<Car>()
                for (doc in result) {
                    val car = Car(
                        doc.get("id_mobil").toString(), "",
                        doc.get("nama").toString(),
                        doc.get("harga").toString(),"", "","",
                        doc.get("kategori").toString(),
                        doc.get("status").toString(),"","","","",
                        doc.get("foto_mobil").toString()
                    )
                    dataCar.add(car)
                }
                resultLiveData.value = dataCar
            }
            .addOnFailureListener { exception ->
                // Handle error
            }

        return resultLiveData
    }

    fun delete(id: String): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        val hm = HashMap<String, Any>()
        hm.set("deleted", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))

        firestore.collection("mobil")
            .document(id)
            .update(hm)
            .addOnSuccessListener {
                resultLiveData.value = true
            }
            .addOnFailureListener {
                resultLiveData.value = false
            }

        return resultLiveData
    }

    fun detail(id: String): LiveData<Car> {
        val resultLiveData = MutableLiveData<Car>()

        firestore.collection("mobil")
            .document(id)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val car = Car(
                        doc.get("id_mobil").toString(),
                        doc.get("merk").toString(),
                        doc.get("nama").toString(),
                        doc.get("harga").toString(),
                        doc.get("stnk").toString(),
                        doc.get("tahun").toString(),
                        doc.get("plat").toString(),
                        doc.get("kategori").toString(),
                        doc.get("status").toString(),
                        doc.get("keterangan").toString(),"",
                        doc.get("creared").toString(),
                        doc.get("updated").toString(),
                        doc.get("foto_mobil").toString()
                    )
                    resultLiveData.value = car
                } else {

                }
            }
            .addOnFailureListener { exception ->

            }

        return resultLiveData
    }

    fun edit(car: Car, uri: Uri?): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        val hm = HashMap<String, Any>()
        hm.set("id_mobil", car.id_mobil)
        hm.set("merk", car.merk)
        hm.set("nama", car.nama)
        hm.set("harga", car.harga)
        hm.set("stnk", car.stnk)
        hm.set("tahun", car.tahun)
        hm.set("plat", car.plat)
        hm.set("keterangan", car.keterangan)
        hm.set("kategori", car.kategori)
        hm.set("created", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        hm.set("updated", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        if (uri != Uri.EMPTY) {
            hm.put("foto_mobil", "")
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
                    hm.put("foto_mobil", task.result.toString())

                    firestore.collection("mobil")
                        .document(car.id_mobil)
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
            firestore.collection("mobil")
                .document(car.id_mobil)
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
