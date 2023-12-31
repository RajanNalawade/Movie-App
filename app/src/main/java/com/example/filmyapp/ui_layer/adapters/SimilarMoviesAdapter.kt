package com.example.filmyapp.ui_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.SimilarMovie
import com.example.filmyapp.databinding.CustomRowBinding

class SimilarMoviesAdapter(
    private val similarList: List<SimilarMovie>,
    private val clickListener: ((SimilarMovie, Int) -> Unit)? = null
) : RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        holder.bindData(similarList[position])
    }

    override fun getItemCount(): Int = similarList.size

    inner class SimilarMovieViewHolder(private val binding: CustomRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                clickListener?.invoke(similarList[adapterPosition], adapterPosition)
            }
        }

        fun bindData(similarMovie: SimilarMovie) {
            binding.movieName.text = similarMovie.title

            binding.root.context.let {
                Glide.with(it)
                    .load(it.getString(R.string.movie_poster_url, similarMovie.posterPath))
                    .fitCenter()
                    .into(binding.poster)
            }
        }
    }
}