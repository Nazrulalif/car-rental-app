package com.example.rental_mobil.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rental_mobil.Model.History
import com.example.rental_mobil.Repository.HistoryRepository

class HistoryViewModel : ViewModel() {
    private val historyRepository = HistoryRepository()

    fun loadData(): LiveData<List<History>>  {
        return historyRepository.loadData()
    }

    fun loadRiwayat(idP: String): LiveData<List<History>>  {
        return historyRepository.loadRiwayat(idP)
    }

    fun detail(id: String): LiveData<History> {
        return historyRepository.detail(id)
    }

    fun delete(id: String, idM: String, idS: String): LiveData<Boolean> {
        return historyRepository.delete(id, idM, idS)
    }
}
