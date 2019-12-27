package com.tebs.spgroupweatherpoc.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tebs.spgroupweatherpoc.Model.City
import com.tebs.spgroupweatherpoc.R
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter(
    val items: ArrayList<City>, val context: Context,
    private val mListener: (City) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_city, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCityName.text = items.get(position).areaName
        holder.tvCountryName?.text = items.get(position).countryName
        var item: City = items[position]
        holder.itemView.setOnClickListener(
            {
                mListener.invoke(item)
            })
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvCityName = view.tv_city_name
    val tvCountryName = view.tv_country_name

}