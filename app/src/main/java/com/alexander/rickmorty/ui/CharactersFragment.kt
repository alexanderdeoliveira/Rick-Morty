package com.alexander.rickmorty.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.alexander.rickmorty.R
import com.alexander.rickmorty.databinding.BottomSheetFilterBinding
import com.alexander.rickmorty.databinding.FragmentCharactersBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val charactersViewModel: CharactersViewModel by inject()
    private lateinit var charactersAdapter: CharactersAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_filter -> {
                        showFilterBottomSheet()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupUI()
        getCharacters()
    }

    private fun setupUI() {
        setupListAdapter()
        binding.apply {
            rvCharacters.apply {
                adapter = charactersAdapter
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            }
            swipeRefresh.setOnRefreshListener {
                getCharacters()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setupListAdapter() {
        charactersAdapter = CharactersAdapter {
            findNavController().navigate(R.id.action_charactersFragment_to_characterDetailFragment, bundleOf("character" to it))
        }

        charactersAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                    loadState.append is LoadState.Loading)
                binding.swipeRefresh.isRefreshing = true
            else {
                binding.swipeRefresh.isRefreshing = false
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getCharacters(name: String? = null, status: String? = null) {
        lifecycleScope.launch {
            charactersViewModel.getCharacters(name, status).collectLatest {
                charactersAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun showFilterBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val binding = BottomSheetFilterBinding.inflate(layoutInflater)
        binding.apply {
            etName.setText(charactersViewModel.charactersNameFilter.value)
            when (charactersViewModel.charactersStatusFilter.value) {
                btAlive.text -> {
                    btAlive.isSelected = true
                    btDead.isSelected = false
                    btUnknown.isSelected = false
                }
                btDead.text -> {
                    btDead.isSelected = true
                    btAlive.isSelected = false
                    btUnknown.isSelected = false
                }
                btUnknown.text -> {
                    btUnknown.isSelected = true
                    btDead.isSelected = false
                    btAlive.isSelected = false
                }
            }

            btAlive.setOnClickListener {
                btAlive.isSelected = !btAlive.isSelected
                btDead.isSelected = false
                btUnknown.isSelected = false
            }

            btDead.setOnClickListener {
                btDead.isSelected = !btDead.isSelected
                btAlive.isSelected = false
                btUnknown.isSelected = false
            }

            btUnknown.setOnClickListener {
                btUnknown.isSelected = !btUnknown.isSelected
                btDead.isSelected = false
                btAlive.isSelected = false
            }

            btFilter.setOnClickListener {
                var statusFilter: String? = null
                when {
                    btAlive.isSelected -> {
                        statusFilter = btAlive.text.toString()
                    }
                    btDead.isSelected -> {
                        statusFilter = btDead.text.toString()
                    }
                    btUnknown.isSelected -> {
                        statusFilter = btUnknown.text.toString()
                    }
                }
                dialog.hide()
                getCharacters(etName.text.toString(), statusFilter)
            }
        }
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CHARACTER_KEY = "character"
    }
}
