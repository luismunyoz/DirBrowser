package com.luismunyoz.dirbrowser.app.browser.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.luismunyoz.dirbrowser.R
import com.luismunyoz.dirbrowser.app.browser.BrowserContract
import com.luismunyoz.dirbrowser.app.browser.BrowserViewModel
import com.luismunyoz.dirbrowser.app.browser.list.model.toUIItem
import com.luismunyoz.dirbrowser.app.util.AdaptiveSpacingItemDecoration
import com.luismunyoz.dirbrowser.databinding.FragmentBrowserListBinding
import com.luismunyoz.network.di.BaseURLQualifier
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BrowserListFragment private constructor() : Fragment() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    @BaseURLQualifier
    lateinit var baseUrl: String

    lateinit var adapter: BrowserListAdapter

    companion object {
        private const val FOLDER_ID = "folder_id"
        private const val NAME = "name"
        private const val GRID_SPAN_COUNT = 2

        fun newInstance(folderId: String, name: String) = BrowserListFragment().apply {
            arguments = bundleOf(
                FOLDER_ID to folderId,
                NAME to name
            )
        }
    }

    private val parentViewModel by activityViewModels<BrowserViewModel>()
    private val viewModel by viewModels<BrowserListViewModel>()

    private lateinit var binding: FragmentBrowserListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowserListBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                initViews(binding)

                viewModel
                    .state
                    .flowWithLifecycle(owner.lifecycle)
                    .map { it.itemsState }
                    .distinctUntilChanged()
                    .onEach { render(it) }
                    .launchIn(owner.lifecycleScope)

                viewModel
                    .state
                    .flowWithLifecycle(owner.lifecycle)
                    .map { it.effect }
                    .distinctUntilChanged()
                    .onEach { effect(it) }
                    .launchIn(owner.lifecycleScope)

                viewModel.onEvent(
                    BrowserListContract.Event.OnInitialized(
                        arguments?.getString(
                            FOLDER_ID
                        ) ?: ""
                    )
                )
            }
        })
        return binding.root
    }

    private fun initViews(binding: FragmentBrowserListBinding) {
        adapter = BrowserListAdapter(baseUrl, picasso)
        binding.list.apply {
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
            adapter = this@BrowserListFragment.adapter
            addItemDecoration(
                AdaptiveSpacingItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.spacing_2x),
                    true
                )
            )
        }
        adapter.listener = object : BrowserListAdapter.Listener {

            override fun onItemClicked(id: String) {
                Timber.tag("BrowserListFragment").d("Clicked on item $id")
                viewModel.onEvent(BrowserListContract.Event.OnItemClicked(id))
            }
        }
    }

    private fun render(state: ItemsState) {
        when (state) {
            ItemsState.Empty -> {
                binding.apply {
                    progressCircular.isVisible = false
                    list.isVisible = false
                    error.isVisible = true
                    error.text = getString(R.string.no_items)
                }
            }
            is ItemsState.Error -> {
                binding.apply {
                    progressCircular.isVisible = false
                    list.isVisible = false
                    error.isVisible = true
                    error.text = getString(R.string.error_items, state.error)
                }
            }
            is ItemsState.Loaded -> {
                binding.apply {
                    progressCircular.isVisible = false
                    list.isVisible = true
                    error.isVisible = false
                    adapter.setItems(state.items.map { it.toUIItem() })
                }
            }
            ItemsState.Loading -> {
                binding.apply {
                    progressCircular.isVisible = true
                    list.isVisible = false
                    error.isVisible = false
                }
            }
        }
    }

    private fun effect(effect: BrowserListContract.State.Effect) {
        when (effect) {
            is BrowserListContract.State.Effect.NavigateToItem ->
                parentViewModel.onEvent(BrowserContract.Event.OnItemClicked(effect.item))
            BrowserListContract.State.Effect.None -> {}
        }
        viewModel.onEvent(BrowserListContract.Event.OnEffectConsumed)
    }

    fun getName() = arguments?.getString(NAME) ?: ""
}