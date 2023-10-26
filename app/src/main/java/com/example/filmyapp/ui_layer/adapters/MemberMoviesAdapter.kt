package com.example.filmyapp.ui_layer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.CastMovie
import com.example.filmyapp.databinding.CharCustomRowBinding

class MemberMoviesAdapter(
    private val moviesList: List<CastMovie>,
    private val fixedSize: Boolean,
    private val clickListener: ((CastMovie, Int) -> Unit)? = null
) : RecyclerView.Adapter<MemberMoviesAdapter.CharacterDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterDetailsViewHolder {
        val binding =
            CharCustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterDetailsViewHolder, position: Int) {
        holder.bindData(moviesList[position])
    }

    override fun getItemCount(): Int =
        if (fixedSize) if (moviesList.size >= 6) 6 else moviesList.size else moviesList.size

    inner class CharacterDetailsViewHolder(private val binding: CharCustomRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickListener?.invoke(moviesList[adapterPosition], adapterPosition)
            }
        }

        fun bindData(movie: CastMovie) {
            binding.movieName.text = movie.title
            binding.movieRolePlayed.text = movie.character

            binding.root.context.let {
                Glide.with(it)
                    .load(it.getString(R.string.movie_poster_url, movie.posterPath))
                    .fitCenter()
                    .into(binding.moviePoster)
            }
        }
    }
}