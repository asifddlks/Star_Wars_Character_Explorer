package com.asifddlks.starwarscharacterexplorer.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import com.asifddlks.starwarscharacterexplorer.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    val uid = savedStateHandle.get<String>(PERSON_UID_SAVED_STATE_KEY)

    private val _uiState = MutableStateFlow<PersonUiState>(PersonUiState.Loading)
    val uiState: StateFlow<PersonUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = PersonUiState.Loading
            if (uid != null) {
                characterRepository.character(uid)
                    .collect { person ->
                        _uiState.value = PersonUiState.Success(person)
                    }
            } else {
                _uiState.value = PersonUiState.Error(Exception("Person Uid not found"))
            }
        }
    }

    companion object {
        private const val PERSON_UID_SAVED_STATE_KEY = "personUid"
    }
}

sealed class PersonUiState {
    object Loading : PersonUiState()
    data class Success(val character: CharacterModel) : PersonUiState()
    data class Error(val exception: Throwable) : PersonUiState()
}
