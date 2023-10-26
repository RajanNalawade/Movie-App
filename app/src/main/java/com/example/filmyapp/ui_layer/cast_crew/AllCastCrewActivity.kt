package com.example.filmyapp.ui_layer.cast_crew

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.CastCrew
import com.example.filmyapp.databinding.ActivityFullCastBinding
import com.example.filmyapp.ui_layer.adapters.CastCrewAdapter
import com.example.filmyapp.utility.PreferenceHelper

class AllCastCrewActivity : AppCompatActivity() {

    private var nightMode = false
    private lateinit var binding: ActivityFullCastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        nightMode = PreferenceHelper.isDarkModeEnabled(this)
        if (nightMode) setTheme(R.style.AppTheme_MD3_Dark) else setTheme(R.style.AppTheme_MD3)

        super.onCreate(savedInstanceState)
        binding = ActivityFullCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        binding.title.text = intent?.getStringExtra(CastCrewFragment.TOOLBAR_TITLE)

        val castCrewList =
            intent?.getParcelableArrayListExtra<CastCrew>(CastCrewFragment.CAST_CREW_LIST) as? ArrayList<CastCrew>
        val adapter = castCrewList?.let {
            CastCrewAdapter(it, false) { castCrew, _, _ ->
                val id = when (castCrew) {
                    is CastCrew.CastData -> castCrew.cast.id
                    is CastCrew.CrewData -> castCrew.crew.id
                }
                val intent = Intent(this, CastCrewDetailsActivity::class.java)
                intent.putExtra(CastCrewFragment.MEMBER_ID, id.toString())
                startActivity(intent)
            }
        }
        adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.adapter = adapter
        if (nightMode) allThemeLogic()
    }

    private fun allThemeLogic() {
        binding.title.setTextColor(Color.parseColor("#bdbdbd"))
    }

    override fun onResume() {
        super.onResume()
        if (nightMode != PreferenceHelper.isDarkModeEnabled(this)) recreate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}