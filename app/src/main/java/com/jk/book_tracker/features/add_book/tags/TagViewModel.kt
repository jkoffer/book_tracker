package com.jk.book_tracker.features.add_book.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.book_tracker.data.local.database.entity.TagEntity
import com.jk.book_tracker.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val repository: TagRepository
) : ViewModel() {
    val tags = repository.tags.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun addTag(tag: TagEntity) {
        viewModelScope.launch {
            repository.addTag(tag)
        }
    }

    fun deleteTag(tagId: String) {
        viewModelScope.launch {
            repository.deleteTag(tagId)
        }
    }
}