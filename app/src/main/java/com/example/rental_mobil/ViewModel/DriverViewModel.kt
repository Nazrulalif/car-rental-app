package com.example.rental_mobil.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rental_mobil.Model.Driver
import com.example.rental_mobil.Repository.DriverRepository

class DriverViewModel : ViewModel() {
    private val driverRepository = DriverRepository()

    fun detail(id: String): LiveData<Driver> {
        return driverRepository.detail(id)
    }
}