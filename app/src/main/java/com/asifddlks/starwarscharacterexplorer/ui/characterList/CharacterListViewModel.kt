package com.asifddlks.starwarscharacterexplorer.ui.characterList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import com.asifddlks.starwarscharacterexplorer.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<PeopleListUiState>(PeopleListUiState.Success(PagingData.empty()))
    val uiState: StateFlow<PeopleListUiState> = _uiState

    init {
        viewModelScope.launch {
            characterRepository.people(PAGE_SIZE)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _uiState.value = PeopleListUiState.Success(pagingData)
                }
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}

sealed class PeopleListUiState {
    data class Success(val pagingData: PagingData<CharacterModel>) : PeopleListUiState()
    data class Error(val exception: Throwable) : PeopleListUiState()
}
