package com.example.filmyapp.ui_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.database.entity.MovieDetails
import com.example.filmyapp.databinding.CustomRowBinding
import toReadableDate

class CollectionsAdapter(
    private var moviesList: ArrayList<MovieDetails>? = null,
    private val clickListener: ((String, String?) -> Unit)? = null,
    private val longClickListener: ((MovieDetails, Int) -> Unit)? = null
) : RecyclerView.Adapter<CollectionsAdapter.SavedMoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        val binding =
            CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {
        moviesList?.let { holder.bindData(it[position]) }
    }

    override fun getItemCount(): Int = moviesList?.size ?: 0

    fun swapData(moviesList: ArrayList<MovieDetails>) {
        this.moviesList = moviesList
        notifyDataSetChanged() //TODO Improvement Required
    }

    fun removeItemAt(position: Int) {
        moviesList?.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class SavedMoviesViewHolder(private val binding: CustomRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.main.setOnClickListener {
                moviesList?.let {
                    clickListener?.invoke(
                        it[adapterPosition].id.toString(),
                        it[adapterPosition].originalTitle
                    )
                }
            }

            binding.main.setOnLongClickListener {
                moviesList?.let { it1 ->
                    longClickListener?.invoke(
                        it1[adapterPosition],
                        adapterPosition
                    )
                }
                true
            }
        }

        fun bindData(movie: MovieDetails) {
            binding.movieName.text = movie.title
            binding.movieYear.text = movie.releaseDate?.toReadableDate()

            binding.root.context.let {
                Glide.with(it)
                    .load(it.getString(R.string.movie_poster_url, movie.posterPath))
                    .into(binding.poster)
            }
        }
    }
}
