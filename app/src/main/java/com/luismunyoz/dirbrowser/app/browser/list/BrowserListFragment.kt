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
import com.luismunyoz.dirbrowser.app.browser.BrowserViewModel
import com.luismunyoz.dirbrowser.app.browser.list.model.toUIItem
import com.luismunyoz.dirbrowser.databinding.FragmentBrowserListBinding
import com.luismunyoz.network.di.BaseURLQualifier
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        fun newInstance(folderId: String) = BrowserListFragment().apply {
            arguments = bundleOf(
                FOLDER_ID to folderId
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
                    .onEach { render(it) }
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
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@BrowserListFragment.adapter
        }
        adapter.listener = object : BrowserListAdapter.Listener {

            override fun onItemClicked(id: String) {
                viewModel.onEvent(
                    BrowserListContract.Event.OnInitialized(
                        arguments?.getString(
                            id
                        ) ?: ""
                    )
                )
            }
        }
    }

    private fun render(state: BrowserListContract.State) {
        when (state.itemsState) {
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
                    error.text = getString(R.string.error_items, state.itemsState.error)
                }
            }
            is ItemsState.Loaded -> {
                binding.apply {
                    progressCircular.isVisible = false
                    list.isVisible = true
                    error.isVisible = false
                    adapter.setItems(state.itemsState.items.map { it.toUIItem() })
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


}