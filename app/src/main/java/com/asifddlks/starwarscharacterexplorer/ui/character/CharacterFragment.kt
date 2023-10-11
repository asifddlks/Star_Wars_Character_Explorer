package com.asifddlks.starwarscharacterexplorer.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.asifddlks.starwarscharacterexplorer.R
import com.asifddlks.starwarscharacterexplorer.databinding.CharacterFragmentBinding
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterFragment : Fragment() {
    private val personViewModel: PersonViewModel by viewModels()

    private var _binding: CharacterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                personViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is PersonUiState.Loading -> {
                            toggleLoading(true)
                        }

                        is PersonUiState.Success -> {
                            toggleLoading(false)
                            showPerson(uiState.character)
                        }

                        is PersonUiState.Error -> {
                            toggleLoading(false)
                            showError(uiState.exception)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.personLoadingProgressBar.show()
        } else {
            binding.personLoadingProgressBar.hide()
        }
    }

    private fun showPerson(character: CharacterModel) {
        binding.personNameTv.text = character.name
        binding.personHeightTv.text = getString(R.string.person_height_label, character.height)
        binding.personMassTv.text = getString(R.string.person_mass_label, character.mass)
        binding.personHairColorTv.text =
            getString(R.string.person_hair_color_label, character.hairColor)
        binding.personSkinColorTv.text =
            getString(R.string.person_skin_color_label, character.skinColor)
        binding.personEyeColorTv.text =
            getString(R.string.person_eye_color_label, character.eyeColor)
        binding.personBirthYearTv.text =
            getString(R.string.person_birth_year_label, character.birthYear)
        binding.personGenderTv.text = getString(R.string.person_gender_label, character.gender)
    }

    private fun showError(exception: Throwable) {
        Snackbar.make(
            requireContext(),
            requireView(),
            exception.localizedMessage,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val TAG = "PersonFragment"
    }
}