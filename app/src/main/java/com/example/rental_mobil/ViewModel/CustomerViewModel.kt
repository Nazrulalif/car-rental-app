package com.example.rental_mobil.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rental_mobil.Model.Customer
import com.example.rental_mobil.Repository.CustomerRepository

class CustomerViewModel : ViewModel() {
    private val customerRepository = CustomerRepository()

    fun detail(id: String): LiveData<Customer> {
        return customerRepository.detail(id)
    }
}