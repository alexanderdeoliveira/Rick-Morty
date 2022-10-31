package com.alexander.rickmorty.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexander.rickmorty.model.db.toCharacterViewObject
import com.alexander.rickmorty.repository.CharacterRepository
import com.alexander.rickmorty.model.viewobject.CharacterViewObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharactersViewModel(
        private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _charactersNameFilter = MutableLiveData<String?>()
    val charactersNameFilter: LiveData<String?> = _charactersNameFilter

    private val _charactersStatusFilter = MutableLiveData<String?>()
    val charactersStatusFilter: LiveData<String?> = _charactersStatusFilter

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCharacters(name: String?, status: String?): Flow<PagingData<CharacterViewObject>> {
        _charactersNameFilter.postValue(name)
        _charactersStatusFilter.postValue(status)
        return characterRepository.getCharacters(name, status).map { it.map { character -> character.toCharacterViewObject() } }.cachedIn(viewModelScope)
    }
}
