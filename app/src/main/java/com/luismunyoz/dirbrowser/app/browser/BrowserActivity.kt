package com.luismunyoz.dirbrowser.app.browser

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.luismunyoz.dirbrowser.R
import com.luismunyoz.dirbrowser.app.browser.list.BrowserListFragment
import com.luismunyoz.dirbrowser.app.util.viewBinding
import com.luismunyoz.dirbrowser.databinding.ActivityBrowserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .map { it.userState }
            .distinctUntilChanged()
            .onEach { render(it) }
            .launchIn(lifecycleScope)
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .map { it.effect }
            .distinctUntilChanged()
            .onEach { effect(it) }
            .launchIn(lifecycleScope)
    }

    private fun render(state: UserState) {
        when (state) {
            is UserState.Loading -> {
                binding.progressCircular.isVisible = true
                binding.error.isVisible = false
                binding.fragmentContainer.isVisible = false
            }
            is UserState.Loaded -> {
                binding.progressCircular.isVisible = false
                binding.error.isVisible = false
                binding.fragmentContainer.isVisible = true
                val user = state.user
                supportActionBar?.title = getString(
                    R.string.title_for_user,
                    user.rootItem.name,
                    "${user.firstName} ${user.lastName}"
                )
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.fragment_container,
                        BrowserListFragment.newInstance(user.rootItem.id, user.rootItem.name)
                    )
                    .commit()
            }
            is UserState.Error -> {
                binding.progressCircular.isVisible = false
                binding.error.isVisible = true
                binding.fragmentContainer.isVisible = false
            }
        }
    }

    private fun effect(effect: BrowserContract.State.Effect) {
        when (effect) {
            is BrowserContract.State.Effect.NavigateToFolder -> {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.fragment_container,
                        BrowserListFragment.newInstance(effect.folder.id, effect.folder.name)
                    )
                    .addToBackStack(effect.folder.id)
                    .commit()
            }
            is BrowserContract.State.Effect.NavigateToImage -> {

            }
            BrowserContract.State.Effect.None -> {}
        }
        viewModel.onEvent(BrowserContract.Event.OnEffectConsumed)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)

            title = getString(R.string.default_title).uppercase(Locale.getDefault())
        }
        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount >= 1)

                val folderName =
                    (supportFragmentManager.fragments.last() as? BrowserListFragment)?.getName()
                title = folderName
            }
        }
    }


}