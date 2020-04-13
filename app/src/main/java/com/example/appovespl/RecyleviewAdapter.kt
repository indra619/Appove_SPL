package com.example.appovespl

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.spl_toko_layout.view.*
import kotlinx.android.synthetic.main.spl_layout.view.*
import kotlinx.coroutines.selects.select

var checked:MutableList<String?> = ArrayList()
var jsonChecked:MutableList<String?> = ArrayList()
class RecyclerViewAdapter(val feed: List<ApprovalpenyimpanganActivity.Penyimpangan>): RecyclerView.Adapter<CustomViewHolder>(){
//    override fun getFilter(): Filter {
//        return object: Filter(){
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val resultList = ArrayList<String>()
//                for(row in feed){
//                    if(row.kodebagian!!.toLowerCase().contains(selectedSpinner!!.toLowerCase())){
//                        resultList.add(row.toString())
//                    }
//                }
//                val Filtered = Filter.FilterResults()
//                Filtered.values = resultList
//                return Filtered
//            }
//
//            override fun publishResults(constraint: CharSequence?, Filtered: FilterResults?) {
//                notifyDataSetChanged()
//            }
//        }
//    }


    override fun getItemCount(): Int {
        return feed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutinflater = LayoutInflater.from(parent?.context)
        val row = layoutinflater.inflate(R.layout.spl_toko_layout, parent, false)
        return CustomViewHolder(row)


    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int){
        val feed = feed.get(position)

        holder.view.textView5.text = feed.namakaryawan + " - " + feed.kodenik + "\n" +
                feed.jamin + " - " + feed.jamout + "\n" +
                "Tanggal : " + feed.tglabsen

//        holder.view.findViewById<Spinner>(R.layout.approvalpenyimpangan.).setOnClickListener{
//            var filtered = ArrayList<Any>()
//
//            var gson2 = GsonBuilder().create().toJson(feed)
//            for(list in gson2){
//                if(feed.kodebagian == selectedSpinner){
//                    filtered.add(list)
//                    println("FILTER")
//                    println(filtered)
//                }
//            }
//        }
//        holder.view.spinner.setOnClickListener {
//
//        }

        holder.view.checkBox.setOnClickListener{
            var gson = GsonBuilder().create().toJson(feed)
            var x = feed.toString().contains(selectedSpinner.toString())
            println(x)
            println(selectedSpinner.toString())
            if(holder.view.checkBox.isChecked){
                checked.add(feed.toString())
                jsonChecked.add(gson)
            } else {
                if(checked.contains(feed.toString())){
                    checked.remove(feed.toString())
                    jsonChecked.remove(gson)
                }
            }
            println(GsonBuilder().create().toJson(feed))
            println(jsonChecked)
//            println(position)
//            println("CLICKED")
//            println(checked)
        }
        holder.view.checkBox.isChecked = checked.contains(feed.toString())
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}