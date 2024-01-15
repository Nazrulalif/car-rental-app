package com.example.rental_mobil.Adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.rental_mobil.View.Admin.Car.CarActivity
import com.example.rental_mobil.View.Admin.Car.CarDetailFragment
import com.example.rental_mobil.Model.Car
import com.example.rental_mobil.R
import com.squareup.picasso.Picasso

class AdapterCar(private var dataList: List<Car>, val parent: CarActivity) :
    RecyclerView.Adapter<AdapterCar.HolderDataMobil>(){
    class HolderDataMobil (v : View) : RecyclerView.ViewHolder(v) {
        val ft = v.findViewById<ImageView>(R.id.carPhoto)
        val sts = v.findViewById<TextView>(R.id.carStatus)
        val nm = v.findViewById<TextView>(R.id.carName)
        val ktg = v.findViewById<TextView>(R.id.carCategory)
        val dt = v.findViewById<Button>(R.id.btnDetailCar)
        val hps = v.findViewById<Button>(R.id.btnDeleteCar)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataMobil {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_car, parent, false)
        return HolderDataMobil(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataMobil, position: Int) {
        val data = dataList.get(position)
        holder.nm.setText(data.nama)
        holder.ktg.setText(data.kategori)
        Picasso.get().load(data.foto_mobil).into(holder.ft)

        if (data.status.equals("tersedia")) {
            holder.sts.setTextColor(Color.parseColor("#0037FF"))
            holder.sts.setText(data.status)
        } else {
            holder.sts.setTextColor(Color.RED)
            holder.sts.setText(data.status)
        }

        holder.dt.setOnClickListener {
            val dialog = CarDetailFragment()

            val bundle = Bundle()
            bundle.putString("id", data.id_mobil)
            dialog.arguments = bundle
            dialog.show(parent.supportFragmentManager, "MobilDetailFragment")
        }

        holder.hps.setOnClickListener {
            parent.idM = data.id_mobil
            parent.deleteMobil()
        }
    }

    fun setData(newDataList: List<Car>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}