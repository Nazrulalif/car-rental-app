package com.example.rental_mobil.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rental_mobil.Model.Car
import com.example.rental_mobil.Repository.CarRepository

class CarViewModel : ViewModel() {
    private val carRepository = CarRepository()

    fun insert(car: Car, uri: Uri?): LiveData<Boolean> {
        return carRepository.insert(car, uri)
    }

    fun loadData(): LiveData<List<Car>> {
        return carRepository.loadData()
    }

    fun delete(id: String): LiveData<Boolean> {
        return carRepository.delete(id)
    }

    fun detail(id: String): LiveData<Car> {
        return carRepository.detail(id)
    }

    fun edit(car: Car, uri: Uri?): LiveData<Boolean> {
        return carRepository.edit(car, uri)
    }
}
