package com.example.rental_mobil.Adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rental_mobil.Model.History
import com.example.rental_mobil.R
import com.example.rental_mobil.View.Customer.HistoryActivity
import com.example.rental_mobil.View.Customer.HistoryDetailActivity

class AdapterHistory(private var dataList: List<History>, val parent: HistoryActivity) :
    RecyclerView.Adapter<AdapterHistory.HolderDataRiwayat>(){
    class HolderDataRiwayat (v : View) : RecyclerView.ViewHolder(v) {
        val mob = v.findViewById<TextView>(R.id.historyCar)
        val tot = v.findViewById<TextView>(R.id.historyTotal)
        val tgl = v.findViewById<TextView>(R.id.historyDate)
        val sts = v.findViewById<TextView>(R.id.historyStatus)
        val rwt = v.findViewById<Button>(R.id.btnDetailHistory)
        val btl = v.findViewById<Button>(R.id.btnCancelRental)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataRiwayat {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        return HolderDataRiwayat(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataRiwayat, position: Int) {
        val data = dataList.get(position)
        holder.mob.setText(data.nama_mobil)
        holder.tot.setText("RM "+data.total)
        holder.tgl.setText(data.tgl_rental)
        holder.sts.setText(data.status_rental)

        holder.rwt.setOnClickListener {
            val intent = Intent(it.context, HistoryDetailActivity::class.java)
            intent.putExtra("id", data.id_rental)
            it.context.startActivity(intent)
        }

        holder.btl.setOnClickListener {
            parent.delete(data.id_rental, data.id_mobil, data.id_sopir)
        }

        val sts = data.status_rental
        if (sts.equals("Booking")) {
            holder.sts.setText(sts)
        } else if (sts.equals("Berjalan")) {
            holder.sts.setText(sts)
            holder.btl.visibility = View.GONE
        } else if (sts.equals("Selesai")) {
            holder.sts.setText(sts)
            holder.sts.setTextColor(Color.GREEN)
            holder.btl.visibility = View.GONE
        }
    }

    fun setData(newDataList: List<History>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}