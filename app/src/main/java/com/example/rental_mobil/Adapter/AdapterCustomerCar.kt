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
import com.example.rental_mobil.Customer.CarDetailFragment
import com.example.rental_mobil.Customer.CarActivity
import com.example.rental_mobil.R
import com.squareup.picasso.Picasso

class AdapterCustomerCar (val dataMobil: List<HashMap<String,String>>, val parent: CarActivity) :
    RecyclerView.Adapter<AdapterCustomerCar.HolderDataMobil>(){
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
        return dataMobil.size
    }

    override fun onBindViewHolder(holder: HolderDataMobil, position: Int) {
        val data = dataMobil.get(position)
        holder.nm.setText(data.get("nama"))
        holder.ktg.setText(data.get("kategori"))
        Picasso.get().load(data.get("foto_mobil")).into(holder.ft)

        if (data.get("status").toString().equals("tersedia")) {
            holder.sts.setTextColor(Color.parseColor("#0037FF"))
            holder.sts.setText(data.get("status"))
        } else {
            holder.sts.setTextColor(Color.RED)
            holder.sts.setText(data.get("status"))
        }

        holder.hps.visibility = View.GONE

        holder.dt.setOnClickListener {
            val dialog = CarDetailFragment()

            val bundle = Bundle()
            bundle.putString("id", data.get("id").toString())
            dialog.arguments = bundle
            dialog.show(parent.supportFragmentManager, "MobilDetailFragment")
        }
    }
}