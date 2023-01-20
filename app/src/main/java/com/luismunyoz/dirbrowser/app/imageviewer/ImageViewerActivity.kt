package com.luismunyoz.dirbrowser.app.imageviewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.luismunyoz.dirbrowser.app.util.viewBinding
import com.luismunyoz.dirbrowser.databinding.ActivityImageViewerBinding
import com.luismunyoz.network.di.BaseURLQualifier
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageViewerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_NAME = "name"
        private const val EXTRA_ID = "id"

        @JvmStatic
        fun getIntent(context: Context, name: String, id: String) =
            Intent(context, ImageViewerActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        EXTRA_NAME to name,
                        EXTRA_ID to id
                    )
                )
            }
    }

    @Inject
    lateinit var picasso: Picasso

    @Inject
    @BaseURLQualifier
    lateinit var baseUrl: String

    private val binding: ActivityImageViewerBinding by viewBinding(ActivityImageViewerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)

            title = intent.getStringExtra(EXTRA_NAME)
        }
    }

    private fun setupUI() {
        val id = intent.getStringExtra(EXTRA_ID)
        picasso
            .load("$baseUrl/items/$id/data")
            .fit()
            .centerInside()
            .into(binding.image)
    }
}