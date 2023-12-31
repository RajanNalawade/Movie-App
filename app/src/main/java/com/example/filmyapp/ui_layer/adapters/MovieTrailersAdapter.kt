package com.example.filmyapp.ui_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.TrailerData
import com.example.filmyapp.databinding.SingleTrailerViewBinding

class MovieTrailersAdapter(
    private val trailers: Array<TrailerData>,
    private val clickListener: ((TrailerData) -> Unit)? = null
) : RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val binding =
            SingleTrailerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bindData(trailers[position])
    }

    override fun getItemCount(): Int = trailers.size

    inner class TrailerViewHolder(private val binding: SingleTrailerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                clickListener?.invoke(trailers[adapterPosition])
            }
        }

        fun bindData(trailer: TrailerData) {
            binding.trailerTitle.text = trailer.title

            binding.root.context.let {
                Glide.with(it)
                    .load(it.getString(R.string.trailer_img_url, trailer.url))
                    .fitCenter()
                    .into(binding.detailYoutube)
            }
        }
    }
}