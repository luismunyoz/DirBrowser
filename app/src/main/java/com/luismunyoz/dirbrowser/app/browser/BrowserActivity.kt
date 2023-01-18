package com.luismunyoz.dirbrowser.app.browser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.luismunyoz.dirbrowser.R
import com.luismunyoz.dirbrowser.app.util.viewBinding
import com.luismunyoz.dirbrowser.databinding.ActivityBrowserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class BrowserActivity : AppCompatActivity() {

    private val viewModel: BrowserViewModel by viewModels()
    private val binding: ActivityBrowserBinding by viewBinding(ActivityBrowserBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state
            .onEach { render(it) }
            .launchIn(lifecycleScope)
    }

    private fun render(state: BrowserContract.State) {
        when (state.userState) {
            is UserState.Loading -> {
                binding.progressCircular.isVisible = true
                binding.error.isVisible = false
            }
            is UserState.Loaded -> {
                binding.progressCircular.isVisible = false
                binding.error.isVisible = false
                val user = state.userState.user
                supportActionBar?.title = getString(R.string.title_for_user, "${user.firstName} ${user.lastName}")
            }
            is UserState.Error -> {
                binding.progressCircular.isVisible = false
                binding.error.isVisible = true
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(
                AppCompatResources.getDrawable(
                    this@BrowserActivity,
                    R.drawable.ic_baseline_arrow_back_24
                )
            )

            title = getString(R.string.default_title).uppercase(Locale.getDefault())
        }
    }
}