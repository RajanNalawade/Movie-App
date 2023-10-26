package com.example.filmyapp.ui_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.database.entity.Movie
import com.example.filmyapp.databinding.CustomRowBinding
import toReadableDate

class MoviesAdapter(
    private var movies: List<Movie> = emptyList(),
    private val clickListener: ((Movie) -> Unit)? = null
) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bindData(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun swapData(newMovies: List<Movie>) {
        this.movies = newMovies
        notifyDataSetChanged() // TODO improvement required
    }

    inner class MoviesViewHolder(private val binding: CustomRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.main.setOnClickListener {
                clickListener?.invoke(movies[adapterPosition])
            }
        }

        fun bindData(movie: Movie) {
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