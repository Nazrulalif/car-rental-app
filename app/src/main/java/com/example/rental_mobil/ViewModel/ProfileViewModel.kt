package com.example.rental_mobil.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rental_mobil.Model.Customer
import com.example.rental_mobil.Repository.ProfilRepository

class ProfileViewModel : ViewModel() {
    private val profilRepository = ProfilRepository()

    fun profil(email: String): LiveData<Customer> {
        return profilRepository.profil(email)
    }

    fun edit(customer: Customer, uri: Uri?) : LiveData<Boolean> {
        return profilRepository.edit(customer, uri)
    }
}