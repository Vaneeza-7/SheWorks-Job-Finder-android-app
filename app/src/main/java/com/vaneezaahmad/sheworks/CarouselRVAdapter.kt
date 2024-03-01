package com.vaneezaahmad.sheworks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarouselRVAdapter(private val carouselDataList: ArrayList<CarouselItem>) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
       val actualPosition = position % carouselDataList.size
        val currentItem = carouselDataList[actualPosition]
        holder.textView.text = currentItem.text
        holder.imageView.setImageResource(currentItem.imageResId)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }
}
