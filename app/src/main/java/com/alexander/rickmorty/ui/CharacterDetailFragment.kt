package com.alexander.rickmorty.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexander.rickmorty.BR
import com.alexander.rickmorty.databinding.FragmentCharacterDetailBinding
import com.alexander.rickmorty.model.viewobject.CharacterViewObject
import com.alexander.rickmorty.ui.CharactersFragment.Companion.CHARACTER_KEY

class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupUI() {
        val character = arguments?.getParcelable<CharacterViewObject>(CHARACTER_KEY)
        binding.apply {
            character?.let {
                setVariable(BR.character, it)
                executePendingBindings()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
