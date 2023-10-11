package com.asifddlks.starwarscharacterexplorer.ui.characterList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.asifddlks.starwarscharacterexplorer.databinding.CharacterListFragmentBinding
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import com.asifddlks.starwarscharacterexplorer.ui.common.NetworkLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterListFragment : Fragment() {
    private val peopleListViewModel: PeopleListViewModel by viewModels()

    private var _binding: CharacterListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharacterListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterListAdapter = CharacterListAdapter { person -> onPersonClicked(person) }

        val swipeRefreshLayout = binding.peopleListSrl
        swipeRefreshLayout.setOnRefreshListener {
            characterListAdapter.refresh()
        }

        val recyclerView = binding.peopleListRv
        recyclerView.adapter =
            characterListAdapter.withLoadStateFooter(NetworkLoadStateAdapter(characterListAdapter::retry))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                peopleListViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is PeopleListUiState.Success -> {
                            swipeRefreshLayout.isRefreshing = false
                            characterListAdapter.submitData(uiState.pagingData)
                        }

                        is PeopleListUiState.Error -> {
                            swipeRefreshLayout.isRefreshing = false
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

    private fun onPersonClicked(character: CharacterModel) {
        val action =
            CharacterListFragmentDirections.actionPeopleListFragmentToPersonFragment(character.uid)
        view?.findNavController()?.navigate(action)
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
        const val TAG = "PeopleListFragment"
    }
}
