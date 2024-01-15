package com.example.rental_mobil.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.rental_mobil.Admin.DriverActivity
import com.example.rental_mobil.Admin.DriverDetailActivity
import com.example.rental_mobil.ImageDetailActivity
import com.example.rental_mobil.R

class AdapterDriver(val dataSopir: List<HashMap<String,String>>, val parent: DriverActivity) :
    RecyclerView.Adapter<AdapterDriver.HolderDataSopir>(){
    class HolderDataSopir (v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.driverName)
        val nik = v.findViewById<TextView>(R.id.driverIc)
        val sts = v.findViewById<TextView>(R.id.driverStatus)
        val ktp = v.findViewById<Button>(R.id.btnIcDriver)
        val sim = v.findViewById<Button>(R.id.btnSimDriver)
        val hps = v.findViewById<Button>(R.id.btnDeleteDriver)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataSopir {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_driver, parent, false)
        return HolderDataSopir(v)
    }

    override fun getItemCount(): Int {
        return dataSopir.size
    }

    override fun onBindViewHolder(holder: HolderDataSopir, position: Int) {
        val data = dataSopir.get(position)
        holder.nm.setText(data.get("nama"))
        holder.nik.setText(data.get("nik"))

        if (data.get("status").toString().equals("tersedia")) {
            holder.sts.setTextColor(Color.parseColor("#0037FF"))
            holder.sts.setText(data.get("status"))
        } else {
            holder.sts.setTextColor(Color.RED)
            holder.sts.setText(data.get("status"))
        }

        holder.ktp.setOnClickListener {
            val intent = Intent(it.context, ImageDetailActivity::class.java)
            intent.putExtra("img", data.get("ktp").toString())
            it.context.startActivity(intent)
            parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        holder.sim.setOnClickListener {
            val intent = Intent(it.context, ImageDetailActivity::class.java)
            intent.putExtra("img", data.get("sim").toString())
            it.context.startActivity(intent)
            parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        holder.cd.setOnClickListener {
            val intent = Intent(it.context, DriverDetailActivity::class.java)
            intent.putExtra("id", data.get("id").toString())
            it.context.startActivity(intent)
            parent.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        holder.hps.setOnClickListener {
            parent.idS = data.get("id").toString()
            parent.deleteSopir()
        }
    }
}